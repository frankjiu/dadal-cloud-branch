package com.modules.Thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

/**
 * @Description: CompletableFutureDemo
 */
public class CompletableFutureDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        final List<Integer> taskList = Lists.newArrayList(2, 1, 3, 4, 6, 5, 7, 8, 9, 10);
        List<String> commitList = new ArrayList<>();
        List<String> completeList = new ArrayList<>();
        try {
            Long start = System.currentTimeMillis();
            /*//方式一:循环创建CompletableFuture list,调用sequence()组装返回一个有返回值的CompletableFuture,返回结果get()获取
            List<CompletableFuture<String>> futureList = new ArrayList<>();
            for (int i = 0; i < taskList.size(); i++) {
                final int j = i;
                //异步执行
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> calculate(taskList.get(j)), pool)
                        .thenApply(e -> Integer.toString(e)) //thenApply的执行结果会作为下一阶段的输入参数
                        //如需获取任务完成先后顺序,此处代码即可
                        .whenComplete((t, e) -> {
                            System.out.println("任务" + t + "完成! result=" + t + "; 异常=" + e + "; " + new Date());
                            completeList.add(t);
                        });
                futureList.add(future);
            }
            commitList = sequence(futureList).get(); // 流式获取结果:此处是根据任务添加顺序获取的结果*/


            //方式二:全流式处理转换成CompletableFuture[].
            CompletableFuture[] futureResult = taskList.stream().map(object -> CompletableFuture.supplyAsync(() -> calculate(object), pool)
                    .thenApply(k -> Integer.toString(k))
                    //如需获取任务完成先后顺序,此处代码即可
                    .whenComplete((t, e) -> {
                        System.out.println("任务" + t + "完成! result=" + t + "; 异常=" + e + "; " + new Date());
                        completeList.add(t);
                    })).toArray(CompletableFuture[]::new);
            //使用无返回值的CompletableFuture包装并进行join等待任务全部执行完毕, 使用 whenComplete()获取返回结果
            CompletableFuture.allOf(futureResult).join();

            System.out.println("###任务完成先后顺序=" + completeList + "; 任务提交顺序=" + commitList + "; 耗时=" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    public static Integer calculate(Integer i) {
        try {
            if (i == 1) {
                Thread.sleep(3000);//任务1耗时3秒
            } else if (i == 5) {
                Thread.sleep(5000);//任务5耗时5秒
            } else {
                Thread.sleep(1000);//其它任务耗时1秒
            }
            System.out.println(">>>>>>[task线程-" + Thread.currentThread().getName() + "]任务 " + i + " 已完成! " + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @Description 将多个CompletableFuture转为一个CompletableFuture, 所有子任务全部完成, 组合后的任务才会完成, 带返回值, 可直接get.
     * 1.构造一个空CompletableFuture,子任务数为入参任务list.size()
     * 2.总任务完成后,每个子任务进行join取结果后转为list
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * @Description 将Stream流式类型futures转为一个CompletableFuture, 所有子任务全部完成, 组合后的任务才会完成. 带返回值, 可直接get.
     */
    public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence(futureList);
    }


}
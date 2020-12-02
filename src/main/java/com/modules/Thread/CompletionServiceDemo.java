package com.modules.Thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description: CompletionService多线程并发任务结果归集(任务先完成可优先获取到, 即结果按照完成先后顺序排序)
 */
public class CompletionServiceDemo {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        int taskCount = 10;
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(pool); // 定义CompletionService
        List<Future<Integer>> futureList = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        try {
            Long start = System.currentTimeMillis();
            for (int i = 0; i < taskCount; i++) {
                futureList.add(completionService.submit(new Task(i + 1))); // 添加任务
            }
            //结果归集方法1:future是提交时返回的, 遍历queue则按照任务提交顺序获取结果
            /*for (Future<Integer> future : futureList) {
                System.out.println("====================");
                Integer result = future.get();//线程在这里阻塞等待该任务执行完毕,按照
                System.out.println("任务result="+result+"获取到结果!"+new Date());
                list.add(result);
            }*/

            //结果归集方法2:内部维护阻塞队列, 任务先完成的先获取到
            for (int i = 0; i < taskCount; i++) {
                Integer result = completionService.take().get();
                System.out.println("获取到任务" + result + "结果!" + new Date());
                list.add(result);
            }

            System.out.println("list=" + list + " 总耗时=" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();//关闭线程池
        }

    }

    static class Task implements Callable<Integer> {
        Integer i;

        public Task(Integer i) {
            super();
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            if (i == 5) {
                Thread.sleep(5000);
            } else {
                Thread.sleep(1000);
            }
            System.out.println(">>>>>>线程[" + Thread.currentThread().getName() + "]执行任务 " + i + " 已完成!");
            return i;
        }

    }
}
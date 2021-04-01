package com.function.completablefuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-03-15
 */
public class FutureAsyncDemo {
    static Random random = new Random();
    static ExecutorService executor = Executors.newFixedThreadPool(4);

    //接收文章名称，获取并计算文章分数
    public static int getArticleScore(String aname) throws ExecutionException, InterruptedException {
        //Future<Integer> futureA1 = executor.submit(new CalculateArticleScoreA());
        //Future<Integer> futureB1 = executor.submit(new CalculateArticleScoreA());
        //Future<Integer> futureC1 = executor.submit(new CalculateArticleScoreA());
        long t1 = System.currentTimeMillis();

        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> CalculateArticleScoreA.call(), executor);
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> CalculateArticleScoreB.call(), executor);
        CompletableFuture<Integer> futureC = CompletableFuture.supplyAsync(() -> CalculateArticleScoreC.call(), executor);

        Integer a = futureA.join();
        Integer b = futureB.join();
        Integer c = futureC.join();

        long t2 = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>>>>" + (t2 - t1));

        executor.shutdown();
        return a + b + c;

    }

    private static void doSomeThingElse() {
        System.out.println("exec other things");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long t1 = System.currentTimeMillis();
        System.out.println(getArticleScore("demo"));
        long t2 = System.currentTimeMillis();
        //System.out.println(">>>>>>>>>>>>>>>" + (t2-t1));
    }
}


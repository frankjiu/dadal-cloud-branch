package com.modules.CompletableFutureTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-04
 */
public class TestExecutorService {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> stringFuture = executor.submit(() -> {
                Thread.sleep(2000);
                return "async thread";
            }
        );
        System.out.println("main thread");
        System.out.println(stringFuture.get());
        executor.shutdown();

    }
}

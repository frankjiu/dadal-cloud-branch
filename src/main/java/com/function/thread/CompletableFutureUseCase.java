package com.function.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-04
 */
public class CompletableFutureUseCase {

    @Test
    public void testSupplyAsync() throws ExecutionException, InterruptedException, TimeoutException {
        /*CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "任务A");
        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> "任务B");
        CompletableFuture<String> futureC = futureB.thenApply(b -> {
            System.out.println("执行任务C.");
            System.out.println("参数:" + b);//参数:任务B
            return "a";
        });
        System.out.println(futureC.get());*/

        /*CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "执行任务A");
        System.out.println(futureA.get());
        futureA.thenRun(() -> System.out.println("执行任务B"));*/

        /*CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> futureB = futureA.thenApply(s -> s + " world");
        CompletableFuture<String> futureC = futureB.thenApply(String::toUpperCase);
        System.out.println(futureC.join());*/

        /*CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> 100d);
        CompletableFuture<Double> futureDiscount = CompletableFuture.supplyAsync(() -> 0.8);
        CompletableFuture<Double> futureResult = futurePrice.thenCombine(futureDiscount, (price, discount) -> price * discount);
        System.out.println("最终价格为:" + futureResult.join()); //最终价格为:80.0

        //cfA.thenCombine(cfB, (resultA, resultB) -> "result A + B");
        //cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {});
        //cfA.runAfterBoth(cfB, () -> {});*/

        /*CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> futureB = futureA.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "world"));
        CompletableFuture<String> future3 = futureB.thenCompose(s -> CompletableFuture.supplyAsync(s::toUpperCase));
        System.out.println(future3.join());*/

        /*CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "通过方式A获取商品";
        });

        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "通过方式B获取商品";
        });

        CompletableFuture<String> futureC = futureA.applyToEither(futureB, product -> "结果:" + product);
        System.out.println(futureC.join()); //结果:通过方式B获取商品*/

        /*CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "futureA result:" + s)
                .exceptionally(e -> {
                    System.out.println(e.getMessage()); //java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                });
        CompletableFuture<String> futureB = CompletableFuture.
                supplyAsync(() -> "执行结果:" + 50)
                .thenApply(s -> "futureB result:" + s)
                .exceptionally(e -> "futureB result: 100");
        System.out.println(futureA.join());//futureA result: 100
        System.out.println(futureB.join());//futureB result:执行结果:50*/

        /*CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .whenComplete((s, e) -> {
                    if (s != null) {
                        System.out.println(s);//未执行
                    }
                    if (e == null) {
                        System.out.println(s);//未执行
                    } else {
                        System.out.println(e.getMessage());//java.lang.ArithmeticException: / by zero
                    }
                })
                .exceptionally(e -> {
                    System.out.println("ex" + e.getMessage()); //ex:java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                });
        System.out.println(futureA.join());//futureA result: 100*/

        /*CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //ex:java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                })
                .whenComplete((s, e) -> {
                    if (e == null) {
                        System.out.println(s);//futureA result: 100
                    } else {
                        System.out.println(e.getMessage());//未执行
                    }
                });
        System.out.println(futureA.join());//futureA result: 100*/

        /*CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                })
                .handle((s, e) -> {
                    if (e == null) {
                        System.out.println(s);//futureA result: 100
                    } else {
                        System.out.println(e.getMessage());//未执行
                    }
                    return "handle result:" + (s == null ? "500" : s);
                });
        System.out.println("..." + futureA.join());//handle result:futureA result: 100*/

        /*CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .handle((s, e) -> {
                    if (e == null) {
                        System.out.println(s);//未执行
                    } else {
                        System.out.println(e.getMessage());//java.lang.ArithmeticException: / by zero
                    }
                    return "handle result:" + (s == null ? "500" : s);
                })
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //未执行
                    return "futureA result: 100";
                });
        System.out.println(futureA.join());//handle result:500*/




        /*ExecutorService executorService = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                long a = 1000 + RandomUtils.nextInt(1, 2000);
                System.out.println("a:" + a);
                thread.sleep(a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "商品详情";
        }, executorService);

        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                long b = 1000 + RandomUtils.nextInt(1, 1000);
                //System.out.println("b:" + b);
                thread.sleep(b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "卖家信息";
        }, executorService);

        CompletableFuture<String> futureC = CompletableFuture.supplyAsync(() -> {
            try {
                thread.sleep(1000 + RandomUtils.nextInt(1, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "库存信息";
        }, executorService);

        CompletableFuture<String> futureD = CompletableFuture.supplyAsync(() -> {
            try {
                thread.sleep(1000 + RandomUtils.nextInt(1, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "订单信息";
        }, executorService);

        // CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureA, futureB, futureC, futureD);
        //allFuture.join();
        *//*CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(futureA, futureB, futureC, futureD);

        Object join = objectCompletableFuture.join();

        System.out.println("最快:" + objectCompletableFuture.get());*//*
        System.out.println(futureA.get(2, TimeUnit.SECONDS));
        //System.out.println(futureA.join() + futureB.join() + futureC.join() + futureD.join());
        System.out.println("总耗时:" + (System.currentTimeMillis() - start));*/

        ExecutorService exec = Executors.newSingleThreadExecutor();
        final Callable call = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return 0;
            }
        };
        final Future future = exec.submit(call);
        try {
            future.get(3000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }



}

package com.function.CompletableFuture;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-03-15
 */
public class CalculateArticleScoreB {
    public static Integer call() {
        //业务代码
        Random random = new Random();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        return random.nextInt(100);
    }

}

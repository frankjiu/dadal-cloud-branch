/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @Package: com.modules.executor   
 * @author: Frankjiu
 * @date: 2020年8月20日
 * @version: V1.0
 */

package com.function.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: ScheduledThreadPoolExecutor 多线程使用测试
 * @author: Frankjiu
 * @date: 2020年8月20日
 */

public class ScheduledThreadPoolExecutorMultiThread {
    private static Integer count = 1;
    MyTimereTask myTimereTask = new MyTimereTask();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    public void tests() {
        try {
            scheduled.scheduleWithFixedDelay(myTimereTask, 1, 1, TimeUnit.MILLISECONDS);
            scheduled.scheduleWithFixedDelay(myTimereTask, 3000, 3000, TimeUnit.MILLISECONDS);
            scheduled.scheduleWithFixedDelay(myTimereTask, 1, 1, TimeUnit.MILLISECONDS);
            scheduled.scheduleWithFixedDelay(myTimereTask, 2000, 2000, TimeUnit.MILLISECONDS);
            scheduled.scheduleWithFixedDelay(myTimereTask, 2000, 2000, TimeUnit.MILLISECONDS);
            while (!scheduled.isTerminated()) {
                lock.readLock().lock();
                if (count > 4) {
                    scheduled.shutdown();
                }
                lock.readLock().unlock();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished all threads");
    }

    private class MyTimereTask implements Runnable {
        @Override
        public void run() {
            lock.writeLock().lock();
            System.out.println("第 " + count + " 次执行任务,count=" + count);
            count++;
            lock.writeLock().unlock();
        }

    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutorMultiThread executor = new ScheduledThreadPoolExecutorMultiThread();
        long t1 = System.currentTimeMillis();
        executor.tests();
        long t2 = System.currentTimeMillis();
        System.out.println(">>>" + (t2-t1)/1000);
    }


}
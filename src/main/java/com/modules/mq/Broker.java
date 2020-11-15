package com.modules.mq;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description: 1.创建使用ArrayBlockingQueue数据模型作为消息自定义中间件的消息载体，及消费生产模式
 * @Author: QiuQiang
 * @Date: 2020-11-14
 */
public class Broker {
    //消息队列存储最大值
    private final static int MAX_SIZE = 3;

    //保存消息数据的容器
    private static ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    //生产消息
    public static void produce(String msg) {
        if (messageQueue.offer(msg)) {
            System.out.println("消息发送成功，msg：" + msg + "队列暂存消息数:" + messageQueue.size());
        } else {
            System.out.println("消息处理数据中心数据达到最大负荷，不能继续放入消息");
        }
        System.out.println("==========================");
    }

    //消费消息
    public static String consume() {
        String msg = messageQueue.poll();
        if (msg != null) {
            //消费条件满足时从消息容器中取出一条消息
            System.out.println("已经消费消息：" + msg + "队列剩余的消息数" + messageQueue.size());
        } else {
            System.out.println("消息处理中心内没有消息可供消费！");
        }
        System.out.println("==========================");
        return msg;
    }
}
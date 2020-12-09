package com.function.mq;

import java.io.IOException;

/**
 * @Description: 5.创建消息接受者
 * @Author: QiuQiang
 * @Date: 2020-11-14
 */
public class ConsumeClient {
    public static void main(String[] args) throws IOException {
        MqClient mq = new MqClient();
        String mes = mq.consume();
        System.out.println("获取的消息为：" + mes);
    }
}
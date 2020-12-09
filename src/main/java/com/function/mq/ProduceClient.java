package com.function.mq;

import java.io.IOException;

/**
 * @Description: 4.创建消息生产者
 * @Author: QiuQiang
 * @Date: 2020-11-14
 */
public class ProduceClient {
    public static void main(String[] args) throws IOException {
        MqClient client = new MqClient();
        client.produce("Hello new World!");
    }
}

package com.function.mq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Description: 3.创建生产及消费方法，模拟消息机制
 * @Author: QiuQiang
 * @Date: 2020-11-14
 */
public class MqClient {

    //生产者
    public static void produce(String message) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), BrokerServer.SERVER_PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(message);
        out.flush();
    }

    //消费者
    public static String consume() throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), BrokerServer.SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        //先向消息列队发送消费标识"CONSUME"字符串
        out.println("CONSUME");
        out.flush();
        //再从消息列队获取一条消息
        String message = in.readLine();
        return message;
    }
}
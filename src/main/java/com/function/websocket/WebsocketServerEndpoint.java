package com.function.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{id}", encoders = {ServerEncoder.class})
@Component
@Slf4j
public class WebsocketServerEndpoint {

    // 在线连接数,应该把它设计成线程安全的
    private static int onlineCount = 0;

    // concurrent包的线程安全Set, 用来存放每个客户端对应的MyWebSocket对象
    // 虽然@Component默认是单例模式的, 但springboot还是会为每个websocket连接初始化一个bean, 所以可以用一个静态set保存起来
    public static CopyOnWriteArraySet<WebsocketServerEndpoint> websocketServerSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话, 需要通过它来给客户端发送数据
    private Session session;

    // 会话窗口的ID标识
    private String id = "";

    /**
     * 建立连接调用
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        websocketServerSet.add(this);
        addOnlineCount();
        log.info("新窗口{}开始监听, 当前在线人数: {}", id, getOnlineCount());
        this.id = id;
        try {
            sendMessage(session, "用户 " + id + " 连接成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 收到消息调用
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // 获取客户消息
        if (StringUtils.isEmpty(message)) {
            sendMessage(session, "客户端未明确消息请求, 请求内容为空!");
        }
        // 查询状态: "1"表示查询状态
        if (message.equals("1")) {
            // 查询状态为 "已付款"
            sendMessage(session, "已付款");
        }

        for (WebsocketServerEndpoint websocketServerEndpoint : websocketServerSet) {
            try {
                websocketServerEndpoint.sendMessage(session, "接收到窗口[" + id + "]的信息: " + message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 关闭连接调用
     */
    @OnClose
    public void onClose() {
        websocketServerSet.remove(this);
        subOnLineCount();
        log.info("连接关闭!");
    }

    /**
     * 连接出错调用
     */
    @OnError
    public void onError(Session session, Throwable e) {
        log.error(">>>>>>>>>>>> error occured!" + e.getMessage(), e);
    }

    /**
     * 服务器主动推送消息 String 类型
     */
    public void sendMessage(Session session, String message) throws IOException {
        if (session == null) {
            sendData(null, message);
            return;
        }
        session.getBasicRemote().sendText(message);
    }

    /**
     * 服务器主动推送消息 Object 类型
     */
    private void sendObject(Object object) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(object);
    }

    /**
     * 服务器群发消息
     */
    public static void sendData(String id, Object obj) {
        for (WebsocketServerEndpoint endpoint : websocketServerSet) {
            try {
                if (id == null) {
                    endpoint.sendObject(obj);
                } else if (endpoint.id.equals(id)) {
                    endpoint.sendObject(obj);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
    }

    private void addOnlineCount() {
        WebsocketServerEndpoint.onlineCount++;
    }

    private void subOnLineCount() {
        WebsocketServerEndpoint.onlineCount--;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

}
package com.function.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.function.websocket.WebsocketServerEndpoint.websocketServerSet;


/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-21
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleMsgTask {

    @Autowired
    private WebsocketServerEndpoint websocketServerEndpoint;

    @Autowired
    private ObjectMapper mapper;

    // 每10秒发送一次
    @Scheduled(cron = "0/10 * * * * ?")
    public void sendTestMessage() {
        if (websocketServerSet.size() > 0) {
            log.info("准备发送数据...");
            try {
                // 这里省略获取到需要发送数据data的逻辑
                InfoDto info = new InfoDto();
                info.setId("567");
                info.setMsg("据最新消息, 奥巴马又上台了!");
                String data = mapper.writeValueAsString(info);
                websocketServerEndpoint.sendMessage(null, data);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }
}


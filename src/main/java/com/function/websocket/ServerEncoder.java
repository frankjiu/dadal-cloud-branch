package com.function.websocket;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class ServerEncoder implements Encoder.Text<Object> {

    @Override
    public void init(EndpointConfig arg0) {
    }

    @Override
    public void destroy() {
    }

    @Override
    @PostConstruct
    public String encode(Object object) {
        try {
            return JSONObject.fromObject(object).toString();
        } catch (Exception e) {
            log.error("websocket编码错误" + e.getMessage(), e);
            return "";
        }
    }

}
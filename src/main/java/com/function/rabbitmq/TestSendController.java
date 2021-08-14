package com.function.rabbitmq;

import com.core.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-13
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestSendController {

    @Autowired
    private OrderMessageSender orderMessageSender;

    @RequestMapping("/sendOrder")
    public HttpResult testSend() {
        Order order = null;
        for (int i = 0; i < 5000; i++) {
            order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setProductId(new Random().nextInt(300));
            order.setAmount(new Random().nextDouble());
            log.info(">>>>>>>>>>>>>>>> 准备发送:" + order);
            orderMessageSender.sendOrder(order);
        }
        return HttpResult.success(order);
    }

    @RequestMapping("/sendMsg")
    public HttpResult sendTransMsg() {
        try {
            orderMessageSender.sendMessage();
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>>>> 消息投送中发生异常, 消息已回滚!");
            log.error(e.getMessage(), e);
        }
        return HttpResult.success();
    }

}

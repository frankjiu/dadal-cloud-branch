package com.function.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 */
@Component
@Slf4j
public class OrderMessageListener {

    // 普通消息消费
    /*@RabbitListener(queues = QUEUE_ORDERS)
    public void receive(String msg, Channel channel, Message message) throws IOException {
        try {
            // int i = 1/0;
            // 手动应答, 以防止消息丢失
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            // DelayKeyInterface.DELAY_EXCHANGE
            log.info("================= 确认收到消息:" + msg);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.error("================= 发送异常, 消息已被拒收:" + msg);
            log.error(e.getMessage(), e);
        }
    }*/

    // 死信队列消息消费
    /*@RabbitListener(queues = DEAD_LETTER_QUEUE)
    public void receive(String msg, Channel channel, Message message) throws IOException {
        try {
            // int i = 1/0;
            // 手动应答, 以防止消息丢失
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            // DelayKeyInterface.DELAY_EXCHANGE
            log.info("================= 确认收到消息:" + msg);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.error("================= 发送异常, 消息已被拒收:" + msg);
            log.error(e.getMessage(), e);
        }
    }*/

}
package com.function.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.function.rabbitmq.RabbitConstants.EXCHANGE_ORDERS;
import static com.function.rabbitmq.RabbitConstants.ROUTING_KEY_ORDERS;


/**
 * 发送消息(限定时间内未消费的信息, 未绑定死信交换机则直接被丢弃)
 */
@Service
@Slf4j
public class OrderMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(EXCHANGE_ORDERS, ROUTING_KEY_ORDERS, order.toString().getBytes(), (message) -> {
            MessageProperties messageProperties = message.getMessageProperties();
            Random random = new Random();
            int randomTime = random.nextInt(600);
            if (randomTime < 300) {
                randomTime = random.nextInt(60);
            }
            log.info(">>>>>>>>>>>>>" + randomTime);
            messageProperties.setExpiration(String.valueOf(randomTime * 1000));
            return message;
        });
    }

    /**
     * 事务消息测试
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "rabbitTransactionManager")
    public void sendMessage() {
        // 将消息通道设置为事务机制
        rabbitTemplate.setChannelTransacted(true);
        String msg = "测试生产者事务消息";
        rabbitTemplate.convertAndSend(EXCHANGE_ORDERS, ROUTING_KEY_ORDERS, msg.getBytes());
        int a = 1 / 0;
        log.info(">>>>>>>>>>>>> 事务消息发送成功!" + msg);
    }

}

package com.function.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.function.rabbitmq.RabbitConstants.*;

/**
 * 查看所有队列 : rabbitmqctl list_queues
 * 根据 queue_name 删除队列: rabbitmqctl delete_queue queue_name
 */
@Configuration
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(RabbitConfig.this);
        rabbitTemplate.setReturnCallback(RabbitConfig.this);
    }

    // 队列
    @Bean(QUEUE_ORDERS)
    Queue queueOrders() {
        // TTL, MAXLENGTH;
        // 设置队列为惰性队列: queueBuilder.lazy(); 以降低内存消耗
        // 设置消息不被持久化: message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT
        return QueueBuilder.durable(QUEUE_ORDERS).ttl(3600 * 1000).deadLetterExchange(DEAD_LETTER_EXCHANGE).deadLetterRoutingKey(DEAD_ROUTING_KEY).build();
    }

    // DirectExchange 交换机
    @Bean(EXCHANGE_ORDERS)
    Exchange exchangeOrders() {
        return ExchangeBuilder.directExchange(EXCHANGE_ORDERS).durable(true).build();
    }

    // 将队列绑定到交换机
    @Bean
    Binding binding(@Qualifier(QUEUE_ORDERS) Queue queue, @Qualifier(EXCHANGE_ORDERS) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_ORDERS);
    }

    // //////////////////////////////////

    // 死信队列
    @Bean(DEAD_LETTER_QUEUE)
    Queue deadQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    // 死信交换机DLX
    @Bean(DEAD_LETTER_EXCHANGE)
    Exchange deadLetterExchange() {
        return ExchangeBuilder.topicExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    // 将死信队列绑定到死信交换机
    @Bean
    Binding bindingDead(@Qualifier(DEAD_LETTER_QUEUE) Queue queue, @Qualifier(DEAD_LETTER_EXCHANGE) TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_ROUTING_KEY);
    }

    /**
     * @param correlationData
     * @param ack(true-发送成功 false-发送失败)
     * @param cause(消息发送失败的原因)
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println(">>>>>>>>>确认发送消息!");
        } else {
            System.out.println(">>>>>>>>>确认发送失败:" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println(">>>>>>>>>=========发送失败的消息:" + message.toString());
    }

    /**
     * 配置事务管理器
     * 事务机制会影响Rabbitmq的性能, 事务机制在一条消息发送之后会使发送端阻塞, 等待MQ响应后才会发送下一条消息, 实际使用较少
     * @param connectionFactory
     * @return
     */
    /*@Bean(name = "rabbitTransactionManager")
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }*/

}
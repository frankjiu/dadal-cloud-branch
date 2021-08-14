package com.function.rabbitmq;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-13
 */
public class RabbitConstants {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_ORDERS = "EXCHANGE_ORDERS";

    /**
     * 队列名称
     */
    public static final String QUEUE_ORDERS = "QUEUE_ORDERS";

    /**
     * 路由键
     */
    public static final String ROUTING_KEY_ORDERS = "ROUTING_KEY_ORDERS";

    /**
     * 死信交换机DLX
     */
    public static final String DEAD_LETTER_EXCHANGE = "DEAD_LETTER_EXCHANGE";

    /**
     * 死信队列DLQ
     */
    public static final String DEAD_LETTER_QUEUE = "DEAD_LETTER_QUEUE";

    /**
     * 死信路由键
     */
    public static final String DEAD_ROUTING_KEY = "DEAD_ROUTING_KEY";


}

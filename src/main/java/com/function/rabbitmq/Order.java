package com.function.rabbitmq;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {

    private String orderNumber;

    private Integer productId;

    private double amount;

}
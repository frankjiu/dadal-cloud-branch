package com.modules.order.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @date: 2021/5/24/0024 10:23
 * @author: YuZHenBo
 */
@Data
public class OrderDetail {

    //唯一主键
    private Long id;
    //订单主键
    private Long orderId;
    //菜品主键
    private Long foodId;
    //菜品数量
    private Integer number;
    //总价
    private BigDecimal totalPrice;
    //创建时间
    private Long createTime;
}

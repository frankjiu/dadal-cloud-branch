package com.modules.order.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 订单临时表
 * @date: 2021/5/28/0028 11:03
 * @author: YuZHenBo
 */
@Data
public class TempOrder {

    //临时订单ID
    private Long id;
    //订单详细信息(包括食品ID,NUMBER)
    private String orderDetail;
    //订单总金额
    private BigDecimal totalCost;
    //创建时间
    private Long createTime;
    //商家ID
    private Long restaurantId;
    //支付方式
    private Integer paymentMode;
    //会员卡号/跨店卡号
    private Long payMemberId;
    //支付宝用户标识
    private String alipayId;
    //微信用户标识
    private String wxOpenID;
    //商户内部微信订单号
    private String wxOrderID;

    private Long createStaff;
}

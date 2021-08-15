package com.modules.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description:
 * @date: 2021/5/21/0021 15:40
 * @author: YuZHenBo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    //唯一主键
    private Long id;
    //订单费用总计
    private BigDecimal totalCost;
    //订单创建时间
    private Long createTime;
    //订单删除时间
    private Long deleteTime;

    private Long restaurantId;

    private Long createStaff;
    //支付渠道：0-微信支付；1-支付宝支付；2-跨店卡支付；3-会员卡支付；4-现金支付；
    private String paymentMode;
    //会员卡ID
    private Long payMemberId;
    //支付宝用户标识
    private String alipayId;
    //微信用户标识
    private String wxOpenID;
    //商户内部微信订单号
    private String wxOrderID;
    //状态: 0-取消订单, 1-订单待支付, 2-订单已支付
    private Integer status;
    //用户奖品记录ID
    private Long rewardId;

}

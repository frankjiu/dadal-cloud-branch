package com.modules.order.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description:
 * @date: 2021/5/21/0021 16:21
 * @author: YuZHenBo
 */
@Data
public class OrderSaveDto {
    //唯一主键
    private Long id;
    //订单费用总计
    private BigDecimal totalCost;
    //订单创建时间
    private Long createTime;
    //订单删除时间
    private Long deleteTime;
    //支付渠道：0-微信支付；1-支付宝支付
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
    //订单详情字符串，拼接规则：  食物ID,数量;食物ID,数量;......"
    @NotNull
    private String orderDetail;
    //商户ID
    @NotNull
    private Long restaurantId;

}

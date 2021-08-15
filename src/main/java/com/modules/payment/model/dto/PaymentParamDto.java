package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/5/27/0027 17:08
 * @author: YuZHenBo
 */
@Data
public class PaymentParamDto {

    //用户ID
    private Long userId;
    //订单ID
    private Long orderId;
    //支付方式：0-微信支付；1-支付宝支付；2-会员卡支付；3-跨店卡管理；4-现金支付；
    private Integer paymentMode;
    //优惠券ID
    private Long rewardId;
    //餐厅ID
    private Long restaurantId;
    //二维码信息
    private String qrCodeString;
}

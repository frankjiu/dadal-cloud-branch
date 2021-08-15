package com.modules.payment.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 支付二维码生成Dto
 * @date: 2021/6/9/0009 14:11
 * @author: YuZHenBo
 */
@Data
public class PaymentCodeDto {

    //会员卡ID
    private String storeId;
    //优惠券ID
    private String couponId;

}

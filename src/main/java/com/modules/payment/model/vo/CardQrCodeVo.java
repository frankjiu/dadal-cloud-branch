package com.modules.payment.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardQrCodeVo {

    //支付类型
    private Integer type;
    //二维码
    private String qrCode;
    //余额
    private BigDecimal balance;
}

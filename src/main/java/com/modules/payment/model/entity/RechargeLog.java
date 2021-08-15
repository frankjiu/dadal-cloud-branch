package com.modules.payment.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-15
 */
@Data
public class RechargeLog {

    private Long id;
    private Long userId;
    private Integer status;
    private String wxOrderId;
    private String aliOrderId;
    private BigDecimal rechargeAmount;
    private BigDecimal rechargeReward;

}

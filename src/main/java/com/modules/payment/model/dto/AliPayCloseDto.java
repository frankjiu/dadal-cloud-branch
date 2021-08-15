package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/11/0011 10:12
 * @author: YuZHenBo
 */
@Data
public class AliPayCloseDto {

    //订单Id
    @NotNull
    private Long orderId;
    //操作员Id
    @NotNull
    private Long operatorId;
}

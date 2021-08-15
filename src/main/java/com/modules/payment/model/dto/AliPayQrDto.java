package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/10/0010 10:05
 * @author: YuZHenBo
 */
@Data
public class AliPayQrDto {

    //订单主键
    @NotNull
    private Long orderId;
}

package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/11/0011 13:35
 * @author: YuZHenBo
 */
@Data
public class WxPayQrDto {

    //订单主键
    @NotNull
    private Long orderID;
}

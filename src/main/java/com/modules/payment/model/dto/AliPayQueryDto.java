package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/11/0011 9:30
 * @author: YuZHenBo
 */
@Data
public class AliPayQueryDto {

    @NotNull(message = "orderID未传入")
    private Long orderId;
}

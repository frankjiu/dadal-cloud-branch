package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/4/0004 13:55
 * @author: YuZHenBo
 */
@Data
public class CashPayDto {

    @NotNull
    private Long orderID;
}

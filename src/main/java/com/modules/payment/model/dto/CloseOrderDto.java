package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/8/0008 9:58
 * @author: YuZHenBo
 */
@Data
public class CloseOrderDto {

    @NotNull
    private Long orderID;
}

package com.modules.payment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/9/0009 16:48
 * @author: YuZHenBo
 */
@Data
public class MemberCardPayDto {

    @NotNull(message = "orderID未传入")
    private Long orderID;

    @NotNull(message = "cardID未传入")
    private String cardID;
}

package com.modules.payment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/11/0011 14:37
 * @author: YuZHenBo
 */
@Data
public class WxPayQueryDto {

    //微信订单ID
    @NotBlank(message = "wxOrderID未传入")
    private String wxOrderId;
}

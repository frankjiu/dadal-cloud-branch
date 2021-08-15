package com.modules.payment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/11/0011 10:55
 * @author: YuZHenBo
 */
@Data
public class WxScanPayDto {

    @NotNull(message = "orderID未传入")
    private Long orderID;
    @NotBlank(message = "authCode未传入")
    private String authCode;
}

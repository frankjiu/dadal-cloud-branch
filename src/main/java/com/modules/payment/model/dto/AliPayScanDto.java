package com.modules.payment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/10/0010 14:41
 * @author: YuZHenBo
 */
@Data
public class AliPayScanDto {

    //订单ID
    @NotNull(message = "orderID未传入")
    private Long orderID;
    //支付宝扫码得到的AuthCode
    @NotBlank(message = "authCode未传入")
    private String authCode;
}

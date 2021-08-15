package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/16/0016 11:42
 * @author: YuZHenBo
 */
@Data
public class CardQrPayDto {

    //二维码信息
    @NotBlank
    private String qrCodeString;
    //订单ID (机器传递)
    @NotNull
    private Long orderId;
}

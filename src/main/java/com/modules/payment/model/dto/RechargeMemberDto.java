package com.modules.payment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/5/0005 17:24
 * @author: YuZHenBo
 */
@Data
public class RechargeMemberDto {
    //会员ID
    @NotNull(message = "cardID")
    private String cardID;
    //充值金额(整数，单位分)
    @NotNull(message = "amount未传入")
    private Long amount;
    //充值方式 wx/ali/cash
    @NotNull(message = "payMode未传入")
    private String payMode;
}

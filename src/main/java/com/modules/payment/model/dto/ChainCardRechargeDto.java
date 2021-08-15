package com.modules.payment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/6/25/0025 14:01
 * @author: YuZHenBo
 */
@Data
public class ChainCardRechargeDto {

    //充值奖励ID
    @NotNull
    private Long id;
    //充值渠道 1微信0支付宝
    @NotBlank
    private String type;
    //商家ID与会员卡ID二选一
    //商家ID
    private Long storeId;
    //会员卡ID
    private Long cardId;

}

package com.modules.login.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 三方登录参数
 * @Author: QiuQiang
 * @Date: 2021-05-27
 */
@Data
public class ThirdLoginDto {

    // 用户三方code码(每次请求时生成, 且只能使用一次)
    @NotBlank
    private String code;

    // 三方登录类型 0:微信, 1:支付宝
    @Range(min = 0, max = 1)
    private int type;

}

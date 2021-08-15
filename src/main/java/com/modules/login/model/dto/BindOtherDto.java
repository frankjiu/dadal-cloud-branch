package com.modules.login.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-15
 */
@Data
public class BindOtherDto {

    // 用户三方code码(每次请求时生成, 且只能使用一次)
    @NotBlank
    private String code;

    // 三方绑定类型: 0微信 1支付宝
    @Range(min = 0, max = 1)
    private int type;

}

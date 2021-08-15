package com.modules.login.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-16
 */
@Data
public class BindPhoneDto {

    // 手机号
    @NotBlank
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误!")
    private String phone;

    // 验证码
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "验证码格式错误!")
    private String code;

    // 用户三方标识
    @NotBlank
    private String openId;

    // 三方绑定类型: 0微信 1支付宝
    @Range(min = 0, max = 1)
    private int type = 0;

}

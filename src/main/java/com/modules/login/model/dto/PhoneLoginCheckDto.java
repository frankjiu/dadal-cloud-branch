package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-27
 */
@Data
public class PhoneLoginCheckDto {

    // 手机号
    @NotBlank
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误!")
    private String mobile;

    // 短信验证码
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "验证码格式错误!")
    private String code;

}

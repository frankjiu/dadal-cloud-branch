package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-18
 */
@Data
public class CheckAccessTokenDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String openId;

}

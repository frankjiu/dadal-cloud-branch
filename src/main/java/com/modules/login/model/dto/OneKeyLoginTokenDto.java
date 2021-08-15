package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-24
 */
@Data
public class OneKeyLoginTokenDto {

    // Android或IOS传递token
    @NotBlank
    private String oneKeyLoginToken;

}

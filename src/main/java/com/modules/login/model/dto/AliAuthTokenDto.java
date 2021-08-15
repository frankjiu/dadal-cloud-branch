package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-19
 */
@Data
public class AliAuthTokenDto {

    @NotBlank
    private String authToken;

}

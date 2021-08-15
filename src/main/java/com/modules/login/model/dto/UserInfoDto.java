package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-26
 */
@Data
public class UserInfoDto {

    // 用户id
    @NotNull
    private Long id;

}

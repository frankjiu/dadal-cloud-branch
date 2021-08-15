package com.modules.login.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @date: 2021/6/30/0030 9:05
 * @author: YuZHenBo
 */
@Data
public class StaffLoginDto {

    //用户名
    @NotBlank
    private String username;
    //密码
    @NotBlank
    private String password;
}

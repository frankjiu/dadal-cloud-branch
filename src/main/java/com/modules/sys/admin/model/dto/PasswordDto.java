package com.modules.sys.admin.model.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
@Data
public class PasswordDto {

    private String oldPassword;
    private String newPassword;

}

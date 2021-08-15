package com.modules.login.model.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-19
 */
@Data
public class EditUserDto {

    // 昵称
    private String nickname;

    // 生日
    private String birthday;

    // 头像二进制
    private String avatar;

    // 手机号
    private String phone;

}

package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-15
 */
@Data
public class PhoneLoginVo {

    // 登录token
    private String token;

    // 是否已经绑定三方
    private boolean bindOther;

}

package com.modules.login.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserInfo {

    // 认证token
    private String access_token;

    // 认证刷新token
    private String refresh_token;

    // 用户多应用关联ID
    private String unionid;

    // 用户三方标识
    private String openid;

    // 应用授权作用域
    private String scope;

    // 过期时间
    private Integer expires_in;

}

package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-15
 */
@Data
public class AccessTokenVo {

    // 用户三方认证token
    private String accessToken;

    // 用户三方标识
    private String openId;

    // 绑定时间
    private Long bindTime;

}

package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-15
 */
@Data
public class ThirdLoginVo {

    // 登录token
    private String token;

    // 是否已经绑定手机
    private boolean bindPhone;

    // 用户三方标识
    private String openId;

}

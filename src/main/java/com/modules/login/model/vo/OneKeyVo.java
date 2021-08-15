package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-24
 */
@Data
public class OneKeyVo {

    // 登录token
    private String token;

    // 是否已经绑定三方
    private boolean bindOther;

}

package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-10
 */
@Data
public class WxErrInfo {

    // 错误码
    private String errcode;

    // 错误消息
    private String errmsg;

}

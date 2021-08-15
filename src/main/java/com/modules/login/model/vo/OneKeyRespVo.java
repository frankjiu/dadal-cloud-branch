package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-24
 */
@Data
public class OneKeyRespVo {

    // 流水号
    private String id;

    // 开发者自定义的id
    private String exID;

    // 响应码
    private String code;

    // 响应信息
    private String content;

    // 加密手机号
    private String phone;

}

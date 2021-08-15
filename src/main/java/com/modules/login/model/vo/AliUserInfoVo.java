package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-19
 */
@Data
public class AliUserInfoVo {

    // 支付宝授权token
    private String token;

    // 真实姓名
    private String username;

    // 手机号
    private String mobile;

    // 昵称
    private String nickname;

    // 生日
    private String birthday;

    // 用户头像地址
    private String headimgurl;

    // 国家
    private String country;

    // 省份
    private String province;

    // 城市
    private String city;

    // 详细地址
    private String address;

    // 区县名称
    private String area;

    // 支付宝用户的userId
    private String userId;

}

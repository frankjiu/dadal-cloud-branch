package com.modules.login.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserInfoVo {

    // 国家
    private String country;

    // 用户多应用公共ID
    private String unionid;

    // 省份
    private String province;

    // 城市
    private String city;

    // 用户三方标识
    private String openid;

    // 性别
    private Integer sex;

    // 昵称
    private String nickname;

    // 头像
    private String headimgurl;

    // 语言类型 zh_CN:简体 zh_TW:繁体 en:英语
    private String language;

    // 权限
    private List<String> privilege;

}

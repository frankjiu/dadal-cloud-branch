package com.modules.gen.model.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;


/**
 * MyBatis-Generator: sys_user
 *
 * @author Administrator
 * @date 2021-08-15 09:15:19
 */
@Data
public class User implements Serializable {
    // 唯一主键
    private Long id;
    // 用户名
    private String username;
    // 昵称
    private String nickname;
    // 国家
    private String country;
    // 省份
    private String province;
    // 城市
    private String city;
    // 语言类型 zh_CN:简体 zh_TW:繁体 en:英语
    private String language;
    // 性别 0:女, 1:男
    private Integer sex;
    // 手机
    private String mobile;
    // 邮箱
    private String email;
    // 头像
    private String headimgurl;
    // 生日
    private String birthday;
    // 创建时间
    private Long createTime;
    // 帐号状态：0-无效，1-有效
    private Integer status;

    private BigDecimal balance;

    private String wxOpenId;
    private Long wxBindTime;
    //用户阿里账号
    private String wxAccount;

    private String appId;
    private String alipayId;
    private Long AliBindTime;
    //用户阿里账号
    private String aliAccount;

    private String mchId;
    //应用授权token
    private String authToken;



    private static final long serialVersionUID = 1L;
}
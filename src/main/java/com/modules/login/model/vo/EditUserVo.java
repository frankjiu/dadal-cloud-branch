package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @date: 2021/7/19/0019 13:53
 * @author: YuZHenBo
 */
@Data
public class EditUserVo {

    //用户ID
    private Long uid;
    //token
    private String token;
    //昵称
    private String nickname;
    //真实姓名
    private String realname;
    //头像(小)
    private String avatar;
    //头像(大)
    private String avatarLarge;
    //年龄
    private String age;
    //性别
    private Integer gender;
    //生日
    private String birthday;
    //简介
    private String intro;
    //手机号
    private String phone;
    //邮箱
    private String mail;
    //注册时间
    private Long regdate;
    //注册时间字符串
    private String regdatestr;
    //状态
    private Integer status;
}

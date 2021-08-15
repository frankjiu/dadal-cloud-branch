package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-17
 */
@Data
public class UserVo {

    private UserDetail user;

    @Data
    public static class UserDetail {
        private Long uid;
        private String token;
        private String nickname;
        private String realname;
        private String avatar;
        private String avatarLarge;
        private String age;
        private Integer gender = 1;
        private String birthday;
        private String intro;
        private String phone;
        private String mail;
        private Long regdate;
        private String regdatestr;
        private Integer status = 1;
    }

}

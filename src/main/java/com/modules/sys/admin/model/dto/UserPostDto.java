package com.modules.sys.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 数据传输包装类
 * @Author: QiuQiang
 * @Date: 2020-12-29
 */
@Data
public class UserPostDto {

    private Long id;                    // ID
    @NotNull
    private String userName;            // 用户名
    private String passWord;            // 密码
    private String salt;                // 盐
    private String email;               // 邮箱
    private String mobile;              // 手机号
    private Integer status;             // 状态 0:禁用 1:正常
    private Long updateTime;            // 更新时间
    @NotNull
    private Long roleType;              // 角色类型

}

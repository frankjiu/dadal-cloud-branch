package com.modules.gen.model.entity;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;


/**
 * MyBatis-Generator: sys_user
 * 
 * @author Administrator
 * @date 2021-08-14 17:12:20
 */
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 盐
     */
    @ApiModelProperty(value = "盐")
    private String salt;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @ApiModelProperty(value = "状态  0：禁用   1：正常")
    private Byte status;

    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    private static final long serialVersionUID = 1L;
}
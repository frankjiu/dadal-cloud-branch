package com.modules.sys.admin.model.entity;

import lombok.Data;

/**
 * @Description: 用户角色
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Data
public class UserRole {

    private Long id;                // ID
    private Long userId;            // 用户ID
    private Long roleId;            // 角色ID

}

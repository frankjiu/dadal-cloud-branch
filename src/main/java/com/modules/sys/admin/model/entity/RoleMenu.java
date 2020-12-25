package com.modules.sys.admin.model.entity;

import lombok.Data;

/**
 * @Description: 角色菜单
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Data
public class RoleMenu {

    private Long id;                // ID
    private Long roleId;            // 角色ID
    private Long menuId;            // 菜单ID

}

package com.modules.sys.admin.service;

import com.modules.sys.admin.model.entity.RoleMenu;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
public interface RoleMenuService {

    int deleteByRoleId(Long rid) throws Exception;

    boolean save(List<RoleMenu> roleMenus) throws Exception;

}

package com.modules.sys.admin.service;

import com.modules.sys.admin.model.entity.UserRole;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public interface UserRoleService {

    int insert(UserRole userRole) throws Exception;

    int update(UserRole userRole) throws Exception;

    UserRole findByUserId(Long id) throws Exception;

    int delete(Long id) throws Exception;

}

package com.modules.sys.admin.service;

import com.modules.sys.admin.model.dto.RoleGetDto;
import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.entity.Role;
import com.modules.sys.admin.model.entity.User;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
public interface RoleService {

    Role findById(Long id) throws Exception;

    List<Role> findPage(RoleGetDto dto) throws Exception;

    int count(RoleGetDto dto) throws Exception;

    boolean save(Role role) throws Exception;

    boolean delete(Long id) throws Exception;
}

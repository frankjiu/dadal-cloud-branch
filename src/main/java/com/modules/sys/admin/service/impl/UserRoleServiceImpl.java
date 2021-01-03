package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.UserRoleDao;
import com.modules.sys.admin.model.entity.UserRole;
import com.modules.sys.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public int insert(UserRole userRole) throws Exception {
        return userRoleDao.insert(userRole);
    }

    @Override
    public int update(UserRole userRole) throws Exception {
        return userRoleDao.update(userRole);
    }

    @Override
    public UserRole findByUserId(Long id) throws Exception {
        return userRoleDao.findByUserId(id);
    }

    @Override
    public int delete(Long id) throws Exception {
        return userRoleDao.delete(id);
    }
}

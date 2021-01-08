package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.RoleMenuDao;
import com.modules.sys.admin.model.entity.RoleMenu;
import com.modules.sys.admin.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public int deleteByRoleId(Long rid) throws Exception {
        return roleMenuDao.deleteByRoleId(rid);
    }

    @Override
    public boolean save(List<RoleMenu> roleMenus) throws Exception {
        int num = roleMenuDao.save(roleMenus);
        if (num == roleMenus.size()) {
            return true;
        }
        return false;
    }


}

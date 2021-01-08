package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.RoleDao;
import com.modules.sys.admin.dao.RoleMenuDao;
import com.modules.sys.admin.model.dto.RoleGetDto;
import com.modules.sys.admin.model.entity.Role;
import com.modules.sys.admin.model.entity.RoleMenu;
import com.modules.sys.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    public Role findById(Long id) throws Exception {
        return roleDao.findById(id);
    }

    @Override
    public List<Role> findPage(RoleGetDto dto) throws Exception {
        return roleDao.findPage(dto);
    }

    @Override
    public int count(RoleGetDto dto) throws Exception {
        return roleDao.count(dto);
    }

    @Override
    public boolean save(Role role) throws Exception {
        int num;
        if (StringUtils.isEmpty(role.getId())) {
            num = roleDao.insert(role);
        } else {
            num = roleDao.update(role);
        }
        if (num == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        int m = roleDao.delete(id);
        List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(id);
        List<Long> menuIds = roleMenus.stream().map(e -> e.getId()).collect(Collectors.toList());
        int n = roleMenuDao.delete(menuIds);
        if (m == 1 && roleMenus.size() == n) {
            transactionManager.commit(status);
            return true;
        }
        transactionManager.rollback(status);
        return false;
    }

}

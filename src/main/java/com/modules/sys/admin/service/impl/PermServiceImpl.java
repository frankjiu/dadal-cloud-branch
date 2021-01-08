package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.MenuDao;
import com.modules.sys.admin.dao.PermDao;
import com.modules.sys.admin.model.dto.PermGetDto;
import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.model.entity.Perm;
import com.modules.sys.admin.service.PermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Service
public class PermServiceImpl implements PermService {

    @Autowired
    private PermDao permDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    public Perm findById(Long id) throws Exception {
        return permDao.findById(id);
    }

    @Override
    public List<Perm> findAll() throws Exception {
        return permDao.findAll();
    }

    @Override
    public List<Perm> findPage(PermGetDto dto) throws Exception {
        return permDao.findPage(dto);
    }

    @Override
    public int count(PermGetDto dto) throws Exception {
        return permDao.count(dto);
    }

    @Override
    public boolean save(Perm perm) throws Exception {
        int menuNum, num;
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        if (StringUtils.isEmpty(perm.getId())) {
            menuNum = 1;
            num = permDao.insert(perm);
        } else {
            // 根据id查找原始perm
            Perm oldPerm = permDao.findById(perm.getId());
            // 根据 perm 名称查找 原始menu
            Menu oldMenu = menuDao.findByPerm(oldPerm.getName());
            if (oldMenu != null) {
                oldMenu.setPerm(perm.getName());
                // 更新原始菜单的权限名称
                menuNum = menuDao.update(oldMenu);
            } else {
                menuNum = 1;
            }
            // 更新权限名称
            num = permDao.update(perm);
        }
        if (menuNum == 1 && num == 1) {
            transactionManager.commit(status);
            return true;
        }
        transactionManager.rollback(status);
        return false;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        int m = permDao.delete(id);
        if (m == 1) {
            return true;
        }
        return false;
    }

}

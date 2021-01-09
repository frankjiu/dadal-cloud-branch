package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.MenuDao;
import com.modules.sys.admin.model.dto.MenuGetDto;
import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public Menu findById(Long id) throws Exception {
        return menuDao.findById(id);
    }

    @Override
    public List<Menu> findPage(MenuGetDto dto) throws Exception {
        return menuDao.findPage(dto);
    }

    @Override
    public int count(MenuGetDto dto) throws Exception {
        return menuDao.count(dto);
    }

    @Override
    public boolean save(Menu menu) throws Exception {
        int num;
        if (StringUtils.isEmpty(menu.getId())) {
            num = menuDao.insert(menu);
        } else {
            num = menuDao.update(menu);
        }
        if (num == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        int m = menuDao.delete(id);
        if (m == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<Menu> findTreeByPid(Long pid) throws Exception{
        return menuDao.findTreeByPid(pid);
    }

    @Override
    public List<Menu> findMenuByRoleId(Long rid) throws Exception {
        // 超级管理员拥有全部权限
        List<Menu> list;
        if (0 == rid) {
            list = menuDao.findAll();
        } else {
            list = menuDao.findMenuByRoleId(rid);
        }
        list.removeAll(Collections.singleton(null));
        return list;
    }

    @Override
    public List<String> findPermsByRoleId(Long rid) throws Exception {
        List<Menu> menus = findMenuByRoleId(rid);
        List<String> perms = menus.stream().map(e -> e.getPerm()).collect(Collectors.toList());
        return perms;
    }

    @Override
    public List<Menu> findAll() throws Exception {
        return menuDao.findAll();
    }

}

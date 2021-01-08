package com.modules.sys.admin.service;

import com.modules.sys.admin.model.dto.MenuGetDto;
import com.modules.sys.admin.model.entity.Menu;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
public interface MenuService {

    Menu findById(Long id) throws Exception;

    List<Menu> findPage(MenuGetDto dto) throws Exception;

    int count(MenuGetDto dto) throws Exception;

    boolean save(Menu menu) throws Exception;

    boolean delete(Long id) throws Exception;

    List<Menu> findTreeByPid(Long pid) throws Exception;
}

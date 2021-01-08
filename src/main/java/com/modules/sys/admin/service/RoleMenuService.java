package com.modules.sys.admin.service;

import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.model.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Mapper
public interface RoleMenuService {

    List<Menu> findMenuByRoleId(Long rid) throws Exception;

    int deleteByRoleId(Long rid) throws Exception;

    boolean save(List<RoleMenu> roleMenus) throws Exception;

}

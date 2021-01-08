package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.model.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Mapper
public interface RoleMenuDao {

    @Select(" SELECT * FROM role_menu WHERE role_id = #{id} ")
    List<RoleMenu> findByRoleId(@Param("id") Long id) throws Exception;

    int delete(@Param("ids") List<Long> ids) throws Exception;

    List<Menu> findMenuByRoleId(@Param("rid") Long rid) throws Exception;

    int deleteByRoleId(@Param("rid") Long rid) throws Exception;

    int save(@Param("roleMenus") List<RoleMenu> roleMenus) throws Exception;
}

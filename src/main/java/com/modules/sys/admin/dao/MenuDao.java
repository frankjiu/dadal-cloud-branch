package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.dto.MenuGetDto;
import com.modules.sys.admin.model.entity.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Mapper
public interface MenuDao {

    @Select(" SELECT * FROM menu WHERE ID = #{id} ")
    Menu findById(@Param("id") Long id) throws Exception;

    List<Menu> findPage(@Param("dto") MenuGetDto dto) throws Exception;

    int count(@Param("dto") MenuGetDto dto) throws Exception;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO menu values(#{id}, #{menu.parentId}, #{menu.type}, " +
            " #{menu.menuName}, #{menu.url}, #{menu.perm}, " +
            " #{menu.orderNum}, #{menu.updateTime})")
    int insert(@Param("menu") Menu menu) throws Exception;

    @Update("UPDATE menu SET PARENT_ID=#{menu.parentId}, TYPE=#{menu.type}, " +
            " MENU_NAME=#{menu.menuName}, URL=#{menu.url}, PERM=#{menu.perm}, " +
            " ORDER_NUM=#{menu.orderNum}, UPDATE_TIME=#{menu.updateTime} " +
            "where ID = #{role.id}")
    int update(@Param("menu") Menu menu) throws Exception;

    @Delete("DELETE FROM menu WHERE ID = #{id}")
    int delete(@Param("id") Long id) throws Exception;

    @Select(" SELECT * FROM menu WHERE PERM = #{perm} ")
    Menu findByPerm(@Param("perm") String perm) throws Exception;

    @Select(" SELECT ID, MENU_NAME menuName, PARENT_ID parentId, URL, ORDER_NUM orderNum FROM menu WHERE PARENT_ID = #{id} ORDER BY ORDER_NUM ASC ")
    List<Menu> findTreeByPid(@Param("pid") Long pid) throws Exception;

}

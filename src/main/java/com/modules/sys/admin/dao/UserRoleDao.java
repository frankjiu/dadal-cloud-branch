package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.entity.UserRole;
import org.apache.ibatis.annotations.*;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
@Mapper
public interface UserRoleDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO user_role values(#{userRole.userId}, #{userRole.roleId} ")
    int insert(@Param("userRole") UserRole userRole) throws Exception;

    @Update(" UPDATE user_role SET ROLE_ID = #{userRole.roleId} WHERE ID = #{userRole.id} ")
    int update(@Param("userRole") UserRole userRole) throws Exception;

    @Select(" SELECT * FROM user_role WHERE USER_ID = #{id} ")
    UserRole findByUserId(@Param("id") Long id) throws Exception;

    @Delete(" DELETE FROM user_role WHERE ID = #{id} ")
    int delete(@Param("id") Long id) throws Exception;

}

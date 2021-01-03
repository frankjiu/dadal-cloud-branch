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
    @Insert("INSERT INTO USER_ROLE values(#{userRole.userId}, #{userRole.roleId} ")
    int insert(@Param("userRole") UserRole userRole) throws Exception;

    @Update(" UPDATE USER_ROLE SET ROLE_ID = #{userRole.roleId} WHERE ID = #{userRole.id} ")
    int update(@Param("userRole") UserRole userRole) throws Exception;

    @Select(" SELECT * FROM USER_ROLE WHERE USER_ID = #{id} ")
    UserRole findByUserId(@Param("id") Long id) throws Exception;

    @Delete(" DELETE FROM USER_ROLE WHERE ID = #{id} ")
    int delete(@Param("id") Long id) throws Exception;

}

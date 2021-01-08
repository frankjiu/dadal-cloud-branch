package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.dto.RoleGetDto;
import com.modules.sys.admin.model.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Mapper
public interface RoleDao {

    @Select(" SELECT * FROM role WHERE ID = #{id} ")
    Role findById(@Param("id") Long id) throws Exception;

    List<Role> findPage(@Param("roleGetDto") RoleGetDto dto) throws Exception;

    int count(@Param("roleGetDto") RoleGetDto dto) throws Exception;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO role values(#{id}, #{role.roleName}, #{role.remark}, #{role.updateTime})")
    int insert(@Param("role") Role role) throws Exception;

    @Update("UPDATE role SET ROLE_NAME=#{role.roleName}, REMARK=#{role.remark}, UPDATE_TIME=#{role.updateTime} " +
            "where ID = #{role.id}")
    int update(@Param("role") Role role) throws Exception;

    @Delete("DELETE FROM role WHERE ID = #{id}")
    int delete(@Param("id") Long id) throws Exception;
    
}

package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.dto.PermGetDto;
import com.modules.sys.admin.model.entity.Perm;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Mapper
public interface PermDao {

    @Select("SELECT * FROM perm WHERE ID = #{id}")
    Perm findById(@Param("id") Long id) throws Exception;

    @Select("SELECT * FROM perm ")
    List<Perm> findAll() throws Exception;

    List<Perm> findPage(@Param("dto") PermGetDto dto) throws Exception;

    int count(@Param("dto") PermGetDto dto) throws Exception;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO perm(PERM_NAME, REMARK, UPDATE_TIME) values(#{perm.permName}, #{perm.remark}, #{perm.updateTime})")
    int insert(@Param("perm") Perm perm) throws Exception;

    @Update("UPDATE perm SET PERM_NAME=#{perm.permName}, REMARK=#{perm.remark}, UPDATE_TIME=#{perm.updateTime} " +
            "where ID = #{perm.id}")
    int update(@Param("perm") Perm perm) throws Exception;

    @Delete("DELETE FROM perm WHERE ID = #{id}")
    int delete(@Param("id") Long id) throws Exception;

}

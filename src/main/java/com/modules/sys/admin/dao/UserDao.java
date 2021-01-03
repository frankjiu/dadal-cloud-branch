package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Mapper
public interface UserDao {
    
    @Select(" SELECT * FROM USER WHERE ID = #{id} ")
    User findById(@Param("id") Long id);

    @Select(" SELECT * FROM USER ")
    List<User> findAll();

    // 使用数据库自增主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO USER values(#{id}, #{user.userName}, #{user.passWord}, #{user.salt}, #{user.email}, #{user.mobile}, #{user.status}, #{user.updateTime})")
    int insert(@Param("user") User user) throws Exception;

    @Update("UPDATE USER SET USER_NAME=#{user.userName}, PASS_WORD=#{user.passWord}, SALT=#{user.salt}, " +
            "EMAIL=#{user.email}, MOBILE=#{user.mobile}, STATUS=#{user.status}, UPDATE_TIME=#{user.updateTime} " +
            "where id = #{user.id}")
    int update(@Param("user") User user) throws Exception;

    @Delete("DELETE FROM DEMO WHERE ID = #{id}")
    int delete(@Param("id") Long id) throws Exception;

    /**
     * 分页条件查询使用xml
     */
    List<User> findPage(@Param("userGetDto") UserGetDto userGetDto);

    int count(@Param("userGetDto") UserGetDto userGetDto);

    @Select(" SELECT DISTINCT name from Perm ")
    Set<String> findPermsById(Long id);

    @Select(" SELECT * FROM USER WHERE USER_NAME = #{userName} ")
    User findByUserName(@Param("userName") String userName);

    @Select(" SELECT * FROM USER WHERE ID = #{userId} AND PASSWORD = #{oldPassword}")
    User findByUserIdAndPassword(Long userId, String oldPassword);

    @Update("UPDATE USER SET PASS_WORD=#{user.passWord} " +
            "WHERE id = #{user.id} AND id = #{user.id} ")
    int updatePassword(@Param("user") User user) throws Exception;

}

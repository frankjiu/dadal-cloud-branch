package com.modules.sys.admin.dao;

import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.model.vo.UserVo;
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
    
    @Select(" SELECT * FROM user WHERE ID = #{id} ")
    User findById(@Param("id") Long id);

    @Select(" SELECT * FROM user ")
    List<User> findAll();

    // 使用数据库自增主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO user values(#{id}, #{user.userName}, #{user.passWord}, " +
            " #{user.salt}, #{user.email}, #{user.mobile}, " +
            " #{user.status}, #{user.updateTime})")
    int insert(@Param("user") User user) throws Exception;

    @Update("UPDATE user SET USER_NAME=#{user.userName}, PASS_WORD=#{user.passWord}, " +
            " SALT=#{user.salt}, EMAIL=#{user.email}, MOBILE=#{user.mobile}, " +
            " STATUS=#{user.status}, UPDATE_TIME=#{user.updateTime} " +
            "where ID = #{user.id}")
    int update(@Param("user") User user) throws Exception;

    @Delete("DELETE FROM user WHERE ID = #{id}")
    int delete(@Param("id") Long id) throws Exception;

    /**
     * 分页条件查询使用xml
     */
    List<UserVo> findPage(@Param("userGetDto") UserGetDto dto);

    int count(@Param("userGetDto") UserGetDto dto);

    @Select(" SELECT DISTINCT NAME from perm ")
    Set<String> findPermsById(@Param("id") Long id);

    @Select(" SELECT * FROM user WHERE USER_NAME = #{userName} ")
    User findByUserName(@Param("userName") String userName);

    @Select(" SELECT * FROM user WHERE ID = #{userId} AND PASSWORD = #{oldPassword}")
    User findByUserIdAndPassword(Long userId, String oldPassword);

    @Update("UPDATE user SET PASS_WORD=#{user.passWord} " +
            "WHERE ID = #{user.id} ")
    int changePassword(@Param("user") User user) throws Exception;

}

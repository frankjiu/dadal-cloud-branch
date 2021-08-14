package com.modules.gen.dao;

import com.modules.gen.model.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 根据主键删除 - sys_user
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 插入对象 - sys_user
     */
    int insert(User record);

    /**
     * 根据主键查询 - sys_user
     */
    User selectByPrimaryKey(Long userId);

    /**
     * 查询所有 - sys_user
     */
    List<User> selectAll();

    /**
     * 根据主键更新 - sys_user
     */
    int updateByPrimaryKey(User record);
}
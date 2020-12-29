package com.modules.sys.admin.service;

import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.entity.User;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
public interface UserService {

    User findById(Long id);

    List<User> findAll();

    int insert(User user) throws Exception;

    int update(User user) throws Exception;

    int delete(Integer id) throws Exception;

    List<User> findPage(UserGetDto userGetDto);

    int count(UserGetDto userGetDto);

    List<String> findPermsById(Long id);

    User findByUserName(String userName);
    
}

package com.modules.sys.admin.service;

import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.dto.UserPostDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.model.vo.UserVo;

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

    boolean save(UserPostDto dto) throws Exception;

    boolean delete(Long id) throws Exception;

    List<UserVo> findPage(UserGetDto dto);

    int count(UserGetDto dto);

    User findByUserName(String userName);

    boolean changePassword(Long userId, String oldPassword, String newPassword) throws Exception;

}

package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.UserDao;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Integer id) {
        return null;
    }

}

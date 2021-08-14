package com.modules.gen.service.impl;

import com.modules.gen.dao.UserMapper;
import com.modules.gen.model.entity.User;
import com.modules.gen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object selectAll() {
        return userMapper.selectAll();
    }
}
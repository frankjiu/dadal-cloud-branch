package com.modules.gen.service;

import com.modules.gen.model.entity.User;

public interface UserService {
    
    Object selectAll();

    void insert(User user);

    User findById(Long userId);

    void update(User user);

    void updateById(User user);

    User findByMobile(String phone);

    User findWxAccount(String openId);

    User findAliAccount(String openId);

}
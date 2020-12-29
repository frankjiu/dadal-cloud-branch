package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.UserDao;
import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public int insert(User user) throws Exception {
        return userDao.insert(user);
    }

    @Override
    public int update(User user) throws Exception {
        return userDao.update(user);
    }

    @Override
    public int delete(Integer id) throws Exception {
        return userDao.delete(id);
    }

    @Override
    public List<User> findPage(UserGetDto userGetDto) {
        return userDao.findPage(userGetDto);
    }

    @Override
    public int count(UserGetDto userGetDto) {
        return userDao.count(userGetDto);
    }

    @Override
    public List<String> findPermsById(Long id) {
        return userDao.findPermsById(id);
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

}

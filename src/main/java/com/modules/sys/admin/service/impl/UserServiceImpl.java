package com.modules.sys.admin.service.impl;

import com.modules.sys.admin.dao.UserDao;
import com.modules.sys.admin.dao.UserRoleDao;
import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.dto.UserPostDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.model.entity.UserRole;
import com.modules.sys.admin.model.vo.UserVo;
import com.modules.sys.admin.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

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
    public boolean save(UserPostDto dto) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setUpdateTime(System.currentTimeMillis() / 1000);
        int m, n;
        if (user.getId() == null) {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setPassWord(new Sha256Hash(user.getPassWord(), salt).toHex());
            user.setSalt(salt);
            m = userDao.insert(user);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(dto.getRoleType());
            n = userRoleDao.insert(userRole);
        } else {
            //查询用户原始数据
            User oldUser = userDao.findById(user.getId());
            user.setPassWord(oldUser.getPassWord());
            user.setSalt(oldUser.getSalt());
            m = userDao.update(user);
            UserRole userRole = userRoleDao.findByUserId(user.getId());
            userRole.setRoleId(dto.getRoleType());
            n = userRoleDao.update(userRole);
        }
        dto.setPassWord(null);
        dto.setSalt(null);
        dto.setId(user.getId());
        if (m == 0 || n == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        int m = userDao.delete(id);
        UserRole userRole = userRoleDao.findByUserId(id);
        int n = userRoleDao.delete(userRole.getId());
        if (m == 1 && n == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<UserVo> findPage(UserGetDto dto) {
        return userDao.findPage(dto);
    }

    @Override
    public int count(UserGetDto dto) {
        return userDao.count(dto);
    }

    @Override
    public Set<String> findPermsById(Long id) {
        return userDao.findPermsById(id);
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) throws Exception {
        User user = userDao.findByUserIdAndPassword(userId, oldPassword);
        if (user == null) {
            return false;
        } else {
            userDao.changePassword(user);
            return true;
        }

    }

}

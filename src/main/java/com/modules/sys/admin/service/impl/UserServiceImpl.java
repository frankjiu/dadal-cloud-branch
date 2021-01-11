package com.modules.sys.admin.service.impl;

import com.core.exception.CommonException;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private DataSourceTransactionManager transactionManager;

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
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
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
            transactionManager.rollback(status);
            return false;
        }
        transactionManager.commit(status);
        return true;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        User user = userDao.findById(id);
        if (user == null) {
            throw new CommonException("record not found!");
        }
        int m = userDao.delete(id);
        UserRole userRole = userRoleDao.findByUserId(id);
        if (userRole == null) {
            throw new CommonException("Current user didn't config any role!");
        }
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
    public User findByUserName(String userName) throws Exception {
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

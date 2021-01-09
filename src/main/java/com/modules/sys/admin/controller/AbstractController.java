package com.modules.sys.admin.controller;

import com.modules.sys.admin.model.entity.RedisUser;
import com.modules.sys.admin.model.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-02
 */
public abstract class AbstractController {

    protected RedisUser getRedisUser() {
        return (RedisUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected User getUser() {
        return getRedisUser().getUser();
    }

    protected Long getUserId() {
        return getUser().getId();
    }

    protected List<String> getPermissions() {
        return getRedisUser().getPermissions();
    }

    protected String getToken() {
        return getRedisUser().getToken();
    }

    protected Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

}
package com.modules.sys.admin.controller;

import com.modules.sys.admin.model.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-02
 */
public abstract class AbstractController {

    protected User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    protected Long getUserId() {
        return getUser().getId();
    }

}
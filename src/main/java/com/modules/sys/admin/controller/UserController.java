package com.modules.sys.admin.controller;

import com.core.result.HttpResult;
import com.modules.base.model.entity.Demo;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@RestController
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("user/{id}")
    public HttpResult findById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(user);
    }

}

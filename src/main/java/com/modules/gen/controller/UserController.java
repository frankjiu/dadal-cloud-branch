package com.modules.gen.controller;

import com.core.anotation.AuthCheck;
import com.core.anotation.Logged;
import com.modules.gen.model.entity.User;
import com.modules.gen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Api(tags = "UserController")
public class UserController {
    
    @Autowired
    private UserService userService;

    @AuthCheck
    @PostMapping("/selectAll")
    @Logged(remark = "查询user列表")
    @ApiOperation(value = "查询user列表")
    public Object selectAll() {
        return userService.selectAll();
    }
}
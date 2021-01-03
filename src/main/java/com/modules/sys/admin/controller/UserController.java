package com.modules.sys.admin.controller;

import com.core.anotation.SysLogged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.PasswordDto;
import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.dto.UserPostDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.model.entity.UserRole;
import com.modules.sys.admin.model.vo.UserVo;
import com.modules.sys.admin.service.UserRoleService;
import com.modules.sys.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@RestController
@Slf4j
@Validated
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * find
     */
    @SysLogged(description = "user.findById")
    @GetMapping("user/{id}")
    public HttpResult findById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(user);
    }

    /**
     * page
     */
    @SysLogged(description = "user.findPage")
    // @RequiresPermissions("user:user:page")
    @PostMapping("user/page")
    public HttpResult findPage(@RequestBody @Valid UserGetDto userGetDto) {
        List<User> userList = userService.findPage(userGetDto);
        int total = userService.count(userGetDto);
        List<UserVo> userVoList = userList.stream()
                .map(e -> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(e, userVo);
                    userVo.setPassWord(null);
                    userVo.setSalt(null);
                    return userVo;
                }).collect(Collectors.toList());
        PageModel<UserVo> pageModel = new PageModel<>();
        pageModel.setData(userVoList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    /**
     * save
     */
    @SysLogged(description = "user.save")
    @RequestMapping(value = "user", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid UserPostDto userPostDto) throws Exception {
        // 查询登录用户角色. 如果当前用户不是超级管理员: 不准创建或修改超级管理员信息
        UserRole currentUserRole = userRoleService.findByUserId(getUserId());
        if (0 != currentUserRole.getRoleId() && 0 == userPostDto.getRoleType()) {
            return HttpResult.fail("You have no permission to operate!");
        }

        // data convert
        User user = new User();
        BeanUtils.copyProperties(userPostDto, user);
        user.setUpdateTime(System.currentTimeMillis() / 1000);
        int x;
        int y;
        if (user.getId() == null) {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setPassWord(new Sha256Hash(user.getPassWord(), salt).toHex());
            user.setSalt(salt);
            x = userService.insert(user);

            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(userPostDto.getRoleType());
            y = userRoleService.insert(userRole);
        } else {
            x = userService.update(user);

            UserRole userRole = userRoleService.findByUserId(user.getId());
            userRole.setRoleId(userPostDto.getRoleType());
            y = userRoleService.update(userRole);
        }
        if (x <= 0 || y <= 0) {
            return HttpResult.fail("save failed!");
        }
        return HttpResult.success(user.getId());
    }

    /**
     * update password of login user
     */
    @PostMapping("password")
    public HttpResult password(@RequestBody @Valid PasswordDto form) throws Exception {
        String oldPassword = new Sha256Hash(form.getOldPassword(), getUser().getSalt()).toHex();
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
        boolean flag = userService.updatePassword(getUserId(), oldPassword, newPassword);
        if (!flag) {
            return HttpResult.fail("Input password is not correct!");
        }
        return HttpResult.success();
    }

    /**
     * delete
     */
    @SysLogged(description = "user.delete")
    @DeleteMapping("user/{id}")
    public HttpResult delete(@PathVariable Long id) throws Exception {
        int x = userService.delete(id);
        UserRole userRole = userRoleService.findByUserId(id);
        int y = userRoleService.delete(userRole.getId());
        if (x <= 0 && y <= 0) {
            return HttpResult.fail("delete failed");
        }
        return HttpResult.success();
    }

}

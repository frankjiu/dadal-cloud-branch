package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 用户管理
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequiresPermissions("user:info")
    @Logged(description = "user.findById")
    @GetMapping("/{id}")
    public HttpResult findById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(user);
    }

    @RequiresPermissions("user:info")
    @Logged(description = "user.findPage")
    @PostMapping("/page")
    public HttpResult findPage(@RequestBody @Valid UserGetDto userGetDto) {
        List<UserVo> userList = userService.findPage(userGetDto);
        int total = userService.count(userGetDto);
        List<UserVo> userVoList = userList.stream()
                .map(e -> {
                    e.setPassWord(null);
                    e.setSalt(null);
                    return e;
                }).collect(Collectors.toList());
        PageModel pageModel = new PageModel<>();
        pageModel.setData(userVoList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    @RequiresPermissions("user:save")
    @Logged(description = "user.save")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid UserPostDto dto) throws Exception {
        // 查询登录用户角色. 如果当前用户不是超级管理员(roleId == 0): 不准创建或修改超级管理员信息
        /*UserRole currentUserRole = userRoleService.findByUserId(getUserId());
        if (0 != currentUserRole.getRoleId() && 0 == dto.getRoleType()) {
            return HttpResult.fail("You have no permission to operate!");
        }*/
        boolean saved = userService.save(dto);
        if (saved) {
            return HttpResult.success(dto);
        }
        return HttpResult.fail("save failed!");
    }

    @RequiresPermissions("user:delete")
    @Logged(description = "user.delete")
    @DeleteMapping("/{id}")
    public HttpResult delete(@PathVariable Long id) throws Exception {
        // 根据用户ID查询用户角色
        UserRole userRole = userRoleService.findByUserId(id);
        Long optRoleId = null;
        if (userRole != null) {
            optRoleId = userRole.getId();
        }
        // 不准删除超级管理员
        UserRole currentUserRole = userRoleService.findByUserId(getUserId());
        if (0 != currentUserRole.getRoleId() && 0 == optRoleId) {
            return HttpResult.fail("You can not delete the administrator!");
        }
        boolean deleted = userService.delete(id);
        if (deleted) {
            return HttpResult.success();
        }
        return HttpResult.fail("delete failed");
    }

    @RequiresPermissions("user:save")
    @Logged(description = "user.changePassword")
    @PostMapping("/changePassword")
    public HttpResult changePassword(@RequestBody @Valid PasswordDto form) throws Exception {
        String oldPassword = new Sha256Hash(form.getOldPassword(), getUser().getSalt()).toHex();
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
        boolean changed = userService.changePassword(getUserId(), oldPassword, newPassword);
        if (changed) {
            return HttpResult.success();
        }
        return HttpResult.fail("Input password is not correct!");
    }


}

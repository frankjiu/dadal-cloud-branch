package com.modules.sys.admin.controller;

import com.core.anotation.SysLogged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.UserGetDto;
import com.modules.sys.admin.model.dto.UserPostDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.model.vo.UserVo;
import com.modules.sys.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
public class UserController {

    @Autowired
    private UserService userService;

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
        // data convert
        User user = new User();
        BeanUtils.copyProperties(userPostDto, user);
        int num = 0;
        if (user.getId() == null) {
            num = userService.insert(user);
        } else {
            num = userService.update(user);
        }
        if (num <= 0) {
            return HttpResult.fail("save failed!");
        }
        return HttpResult.success(user.getId());
    }

    /**
     * delete
     */
    @SysLogged(description = "user.delete")
    @DeleteMapping("user/{id}")
    public HttpResult delete(@PathVariable Integer id) throws Exception {
        int num = userService.delete(id);
        if (num <= 0) {
            return HttpResult.fail("delete failed");
        }
        return HttpResult.success();
    }

}

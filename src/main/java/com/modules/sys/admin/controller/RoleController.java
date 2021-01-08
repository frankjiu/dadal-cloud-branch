package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.RoleGetDto;
import com.modules.sys.admin.model.dto.RolePostDto;
import com.modules.sys.admin.model.entity.Role;
import com.modules.sys.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 角色管理
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("role:info")
    @Logged(description = "role.findById")
    @GetMapping("/{id}")
    public HttpResult findById(@PathVariable Long id) throws Exception {
        Role role = roleService.findById(id);
        if (role == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(role);
    }

    @RequiresPermissions("role:info")
    @Logged(description = "role.findPage")
    @PostMapping("/page")
    public HttpResult findPage(@RequestBody @Valid RoleGetDto dto) throws Exception {
        List<Role> roleList = roleService.findPage(dto);
        int total = roleService.count(dto);
        PageModel pageModel = new PageModel<>();
        pageModel.setData(roleList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    @RequiresPermissions("role:save")
    @Logged(description = "role.save")
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid RolePostDto dto) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setUpdateTime(System.currentTimeMillis() / 1000);
        boolean saved = roleService.save(role);
        if (saved) {
            return HttpResult.success(role);
        }
        return HttpResult.fail();
    }

    @RequiresPermissions("role:delete")
    @Logged(description = "role.delete")
    @DeleteMapping("/{id}")
    public HttpResult delete(@PathVariable Long id) throws Exception {
        Role role = roleService.findById(id);
        if (role == null) {
            return HttpResult.fail("Record is not exist!");
        }
        boolean deleted = roleService.delete(id);
        if (deleted) {
            return HttpResult.success(id);
        }
        return HttpResult.fail();
    }

}

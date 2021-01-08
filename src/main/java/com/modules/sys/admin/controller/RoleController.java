package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.RoleGetDto;
import com.modules.sys.admin.model.dto.RolePostDto;
import com.modules.sys.admin.model.entity.Role;
import com.modules.sys.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Logged(description = "role.findById")
    @GetMapping("role/{id}")
    public HttpResult findById(@PathVariable Long id) throws Exception {
        Role role = roleService.findById(id);
        if (role == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(role);
    }

    @Logged(description = "role.findPage")
    @PostMapping("role/page")
    public HttpResult findPage(@RequestBody @Valid RoleGetDto dto) throws Exception {
        List<Role> roleList = roleService.findPage(dto);
        int total = roleService.count(dto);
        PageModel pageModel = new PageModel<>();
        pageModel.setData(roleList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    @Logged(description = "role.save")
    @RequestMapping(value = "role", method = {RequestMethod.POST, RequestMethod.PUT})
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

    @Logged(description = "role.delete")
    @DeleteMapping("role/{id}")
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

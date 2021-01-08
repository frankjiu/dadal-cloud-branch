package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.modules.sys.admin.model.dto.RoleMenuPostDto;
import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.model.entity.RoleMenu;
import com.modules.sys.admin.service.MenuService;
import com.modules.sys.admin.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 授权管理(查看角色菜单权限, 授予角色菜单权限)
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/permission")
public class RoleMenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 查询角色权限
     * @param rid
     * @return
     * @throws Exception
     */
    @RequiresPermissions("permission:info")
    @Logged(description = "permission.findMenuByRoleId")
    @GetMapping("/{id}")
    public HttpResult findMenuByRoleId(@PathVariable("id") Long rid) throws Exception {
        List<Menu> menus = menuService.findMenuByRoleId(rid);
        menus.removeAll(Collections.singleton(null));
        if (CollectionUtils.isEmpty(menus)) {
            return HttpResult.success(new ArrayList<>());
        }
        List<String> perms = menus.stream().map(e -> e.getPerm()).collect(Collectors.toList());
        return HttpResult.success(perms);
    }

    /**
     * 角色授权
     * @param dto
     * @return
     * @throws Exception
     */
    @RequiresPermissions("permission:save")
    @Logged(description = "permission.save")
    @PostMapping()
    public HttpResult save(@RequestBody @Valid RoleMenuPostDto dto) throws Exception {
        // 删除角色原有权限
        Long roleId = dto.getRoleId();
        roleMenuService.deleteByRoleId(roleId);
        // 保存最新权限
        List<Long> menuIds = dto.getMenuIds();
        if (CollectionUtils.isEmpty(menuIds)) {
            return HttpResult.success();
        }
        List<RoleMenu> roleMenus = menuIds.stream().map(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());
        boolean saved = roleMenuService.save(roleMenus);
        if (saved) {
            return HttpResult.success();
        }
        return HttpResult.fail();
    }

}

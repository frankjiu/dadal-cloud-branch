package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.modules.sys.admin.model.dto.RoleMenuPostDto;
import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.model.entity.RoleMenu;
import com.modules.sys.admin.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
public class RoleMenuController {

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 查询角色权限
     * @param rid
     * @return
     * @throws Exception
     */
    @Logged(description = "roleMenu.findMenuByRoleId")
    @GetMapping("roleMenu/{id}")
    public HttpResult findMenuByRoleId(@PathVariable("id") Long rid) throws Exception {
        List<Menu> menus = roleMenuService.findMenuByRoleId(rid);
        return HttpResult.success(menus);
    }

    /**
     * 角色授权
     * @param dto
     * @return
     * @throws Exception
     */
    @Logged(description = "roleMenu.save")
    @RequestMapping(value = "roleMenu", method = {RequestMethod.POST})
    public HttpResult save(@RequestBody @Valid RoleMenuPostDto dto) throws Exception {
        // 删除角色原有权限
        Long roleId = dto.getRoleId();
        roleMenuService.deleteByRoleId(roleId);
        // 保存最新权限
        List<Long> menuIds = dto.getMenuIds();
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

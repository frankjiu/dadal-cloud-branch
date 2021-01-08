package com.modules.sys.admin.controller;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.MenuGetDto;
import com.modules.sys.admin.model.dto.MenuPostDto;
import com.modules.sys.admin.model.entity.Menu;
import com.modules.sys.admin.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 菜单管理
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@RestController
@Slf4j
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Logged(description = "menu.findById")
    @GetMapping("menu/{id}")
    public HttpResult findById(@PathVariable Long id) throws Exception {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(menu);
    }

    @Logged(description = "menu.findPage")
    @PostMapping("menu/page")
    public HttpResult findPage(@RequestBody @Valid MenuGetDto dto) throws Exception {
        List<Menu> menuList = menuService.findPage(dto);
        int total = menuService.count(dto);
        PageModel pageModel = new PageModel<>();
        pageModel.setData(menuList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    @Logged(description = "menu.findTreeByPid")
    @PostMapping("menu/tree/{pid}")
    public HttpResult findTreeByPid(@PathVariable Long pid) throws Exception {
        List<Menu> menuTree = menuService.findTreeByPid(pid);
        return HttpResult.success(menuTree);
    }

    @Logged(description = "menu.save")
    @RequestMapping(value = "menu", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid MenuPostDto dto) throws Exception {
        Menu menu = new Menu();
        BeanUtils.copyProperties(dto, menu);
        menu.setUpdateTime(System.currentTimeMillis() / 1000);
        boolean saved = menuService.save(menu);
        if (saved) {
            return HttpResult.success(menu);
        }
        return HttpResult.fail();
    }

    @Logged(description = "menu.delete")
    @DeleteMapping("menu/{id}")
    public HttpResult delete(@PathVariable Long id) throws Exception {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            return HttpResult.fail("Record is not exist!");
        }
        boolean deleted = menuService.delete(id);
        if (deleted) {
            return HttpResult.success(id);
        }
        return HttpResult.fail();
    }

}

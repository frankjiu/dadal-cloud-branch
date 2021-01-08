package com.modules.sys.admin.controller;

import com.core.anotation.SysLogged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.admin.model.dto.PermGetDto;
import com.modules.sys.admin.model.dto.PermPostDto;
import com.modules.sys.admin.model.entity.Perm;
import com.modules.sys.admin.service.PermService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 权限管理
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@RestController
@Slf4j
@Validated
public class PermController {

    @Autowired
    private PermService permService;

    @SysLogged(description = "perm.findById")
    @GetMapping("perm/{id}")
    public HttpResult findById(@PathVariable Long id) throws Exception {
        Perm perm = permService.findById(id);
        if (perm == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(perm);
    }

    @SysLogged(description = "perm.findAll")
    @PostMapping("perm/findAll")
    public HttpResult findAll() throws Exception {
        List<Perm> permList = permService.findAll();
        List<String> nameList = permList.stream().map(e -> e.getName()).collect(Collectors.toList());
        return HttpResult.success(nameList);
    }

    @SysLogged(description = "perm.findPage")
    @PostMapping("perm/page")
    public HttpResult findPage(@RequestBody @Valid PermGetDto dto) throws Exception {
        List<Perm> permList = permService.findPage(dto);
        int total = permService.count(dto);
        PageModel pageModel = new PageModel<>();
        pageModel.setData(permList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    @SysLogged(description = "perm.save")
    @RequestMapping(value = "perm", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid PermPostDto dto) throws Exception {
        Perm perm = new Perm();
        BeanUtils.copyProperties(dto, perm);
        perm.setUpdateTime(System.currentTimeMillis() / 1000);
        boolean saved = permService.save(perm);
        if (saved) {
            return HttpResult.success(perm);
        }
        return HttpResult.fail();
    }

    @SysLogged(description = "perm.delete")
    @DeleteMapping("perm/{id}")
    public HttpResult delete(@PathVariable Long id) throws Exception {
        Perm perm = permService.findById(id);
        if (perm == null) {
            return HttpResult.fail("Record is not exist!");
        }
        boolean deleted = permService.delete(id);
        if (deleted) {
            return HttpResult.success(id);
        }
        return HttpResult.fail();
    }


}

package com.core.log.controller;

import com.core.anotation.Logged;
import com.core.log.model.dto.LogDto;
import com.core.log.model.entity.Log;
import com.core.log.service.LogService;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * @remark:
 * @Author: QiuQiang
 * @Date: 2020-12-21
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @RequiresPermissions("log:info")
    @Logged(remark = "log.findPage")
    @PostMapping("/page")
    public HttpResult findPage(@RequestBody @Valid LogDto sysLogDto) throws Exception {
        List<Log> sysLogList = logService.findPage(sysLogDto);
        int total = logService.count(sysLogDto);
        PageModel<Log> pageModel = new PageModel<>();
        pageModel.setData(sysLogList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

}

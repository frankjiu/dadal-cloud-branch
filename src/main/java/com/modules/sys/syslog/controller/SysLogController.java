package com.modules.sys.syslog.controller;

import com.core.anotation.SysLogged;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.modules.sys.syslog.model.dto.SysLogDto;
import com.modules.sys.syslog.model.entity.SysLog;
import com.modules.sys.syslog.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-21
 */
@RestController
@Slf4j
@Validated
public class SysLogController {

    @Autowired
    SysLogService sysLogService;

    @SysLogged(description = "syslog query")
    // @RequiresPermissions("syslog:syslog:page")
    @PostMapping("syslog/page")
    public HttpResult findPage(@RequestBody @Valid SysLogDto sysLogDto) {
        List<SysLog> sysLogList = sysLogService.findPage(sysLogDto);
        int total = sysLogService.count(sysLogDto);
        PageModel<SysLog> pageModel = new PageModel<>();
        pageModel.setData(sysLogList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

}

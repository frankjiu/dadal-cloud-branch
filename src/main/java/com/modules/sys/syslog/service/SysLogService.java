package com.modules.sys.syslog.service;

import com.modules.sys.syslog.model.dto.SysLogDto;
import com.modules.sys.syslog.model.entity.SysLog;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020/12/21 10:51
 */
public interface SysLogService {

    int save(SysLog sysLog) throws Exception;

    List<SysLog> findPage(SysLogDto sysLogDto);

    int count(SysLogDto sysLogDto);

}

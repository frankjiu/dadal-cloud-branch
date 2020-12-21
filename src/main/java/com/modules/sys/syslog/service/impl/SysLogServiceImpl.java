package com.modules.sys.syslog.service.impl;

import com.modules.sys.syslog.dao.SysLogDao;
import com.modules.sys.syslog.model.dto.SysLogDto;
import com.modules.sys.syslog.model.entity.SysLog;
import com.modules.sys.syslog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-21
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogDao sysLogDao;

    @Override
    public int save(SysLog sysLog) throws Exception {
        return sysLogDao.save(sysLog);
    }

    @Override
    public List<SysLog> findPage(SysLogDto sysLogDto) {
        return sysLogDao.findPage(sysLogDto);
    }

    @Override
    public int count(SysLogDto sysLogDto) {
        return sysLogDao.count(sysLogDto);
    }

}

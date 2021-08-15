package com.core.log.service.impl;

import com.core.log.dao.LogDao;
import com.core.log.model.dto.LogDto;
import com.core.log.model.entity.Log;
import com.core.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-21
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogDao logDao;

    @Override
    public int save(Log log) throws Exception {
        return logDao.save(log);
    }

    @Override
    public List<Log> findPage(LogDto dto) throws Exception {
        return logDao.findPage(dto);
    }

    @Override
    public int count(LogDto dto) throws Exception {
        return logDao.count(dto);
    }

}

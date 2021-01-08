package com.modules.sys.log.service;

import com.modules.sys.log.model.dto.LogDto;
import com.modules.sys.log.model.entity.Log;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020/12/21 10:51
 */
public interface LogService {

    int save(Log log) throws Exception;

    List<Log> findPage(LogDto dto) throws Exception;

    int count(LogDto dto) throws Exception;

}

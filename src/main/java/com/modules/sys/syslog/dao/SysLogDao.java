package com.modules.sys.syslog.dao;

import com.modules.sys.syslog.model.dto.SysLogDto;
import com.modules.sys.syslog.model.entity.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-21
 */
@Mapper
public interface SysLogDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO sys_log(USER_NAME, DESCRIPTION, URL, METHOD, PARAMS, TIME, IP, CREATE_TIME, VERSION) " +
            " values(#{sysLog.userName}, #{sysLog.description}, #{sysLog.url}" +
            " , #{sysLog.method}, #{sysLog.params}, #{sysLog.time}" +
            " , #{sysLog.ip}, #{sysLog.createTime}, #{sysLog.version})")
    int save(@Param("sysLog") SysLog sysLog) throws Exception;

    List<SysLog> findPage(@Param("sysLogDto") SysLogDto sysLogDto);

    int count(@Param("sysLogDto") SysLogDto sysLogDto);
}

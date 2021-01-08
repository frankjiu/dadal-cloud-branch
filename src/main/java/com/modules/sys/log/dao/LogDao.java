package com.modules.sys.log.dao;

import com.modules.sys.log.model.dto.LogDto;
import com.modules.sys.log.model.entity.Log;
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
public interface LogDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO log(USER_NAME, DESCRIPTION, URL, METHOD, PARAMS, TIME, IP, CREATE_TIME, VERSION) " +
            " values(#{log.userName}, #{log.description}, #{log.url}" +
            " , #{log.method}, #{log.params}, #{log.time}" +
            " , #{log.ip}, #{log.createTime}, #{log.version})")
    int save(@Param("log") Log log) throws Exception;

    List<Log> findPage(@Param("dto") LogDto dto) throws Exception;

    int count(@Param("dto") LogDto dto) throws Exception;
}

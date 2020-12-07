/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.sys.dao;

import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Mapper
public interface DemoDao {

    @Select(" SELECT * FROM demo WHERE ID = #{id} ")
    Demo findDemoById(@Param("id") String id) throws Exception;

    @Select(" SELECT * FROM demo WHERE CARD_NAME = #{demoDtoVal.cardName} ")
    List<Demo> getDemoListByCondition(@Param("demoDtoVal") DemoDto demoDto) throws Exception;

    @Select(" SELECT * FROM demo ")
    List<Demo> getDemoList() throws Exception;

    List<Demo> getDemoListFast(@Param("demoDto") DemoDto demoDto) throws Exception;

}

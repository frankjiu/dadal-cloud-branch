/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import org.apache.ibatis.annotations.Select;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Mapper
public interface DemoDao {

    @Select(" SELECT d.* FROM (SELECT CARD_NAME, MAX( CARD_NUMBER ) m FROM demo GROUP BY CARD_NAME ) t, " +
            " demo d WHERE t.CARD_NAME = d.CARD_NAME AND t.m = d.CARD_NUMBER AND d.CARD_NAME = #{demoDtoVal.cardName} ")
    List<Demo> getDemoList(@Param("demoDtoVal") DemoDto demoDto) throws Exception;

    List<Demo> getDemoListFast(@Param("demoDto") DemoDto demoDto) throws Exception;

}

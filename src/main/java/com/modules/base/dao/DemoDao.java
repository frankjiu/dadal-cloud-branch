/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.dao;

import com.modules.base.model.dto.DemoGetDto;
import com.modules.base.model.entity.Demo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description: 使用注解方式(@select和@SelectProvider)进行简单CRUD或统计查询, 复杂的条件查询到xml中进行.
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Mapper
public interface DemoDao {

    /**
     * ################################################################################
     */

    @Select(" SELECT * FROM demo WHERE ID = #{id} ")
    Demo findById(@Param("id") Integer id) throws Exception;

    @Select(" SELECT * FROM demo LIMIT 0, #{limitedCount} ")
    List<Demo> findAll(@Param("limitedCount") Integer limitedCount) throws Exception;

    // 使用数据库自增主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO demo(CARD_NAME, CARD_NUMBER) values(#{demo.cardName}, #{demo.cardNumber})")
    int insert(@Param("demo") Demo demo) throws Exception;

    @Update("UPDATE demo SET CARD_NAME = #{demo.cardName}, CARD_NUMBER = #{demo.cardNumber} where id = #{demo.id}")
    int update(@Param("demo") Demo demo) throws Exception;

    @Delete("DELETE FROM DEMO WHERE ID = #{id}")
    int delete(@Param("id") Integer id) throws Exception;

    /**
     * ################################################################################
     */

    /**
     * 分页条件查询使用xml
     * @param demoGetDto
     * @return
     * @throws Exception
     */
    @Select(" SELECT * FROM demo WHERE CARD_NAME = #{demoGetDto.cardName} " +
            "AND CARD_NUMBER LIKE CONCAT('%',#{demoGetDto.cardNumber},'%') " +
            "LIMIT #{demoGetDto.pageNum}, #{demoGetDto.pageSize}")
    List<Demo> findPage(@Param("demoGetDto") DemoGetDto demoGetDto) throws Exception;

}

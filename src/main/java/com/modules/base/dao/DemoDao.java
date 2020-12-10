/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.dao;

import com.modules.base.model.dto.DemoDto;
import com.modules.base.model.entity.Demo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Mapper
public interface DemoDao {

    @Select(" SELECT * FROM demo ")
    List<Demo> findPage(DemoDto demoDto) throws Exception;

    @Select(" SELECT * FROM demo WHERE ID = #{id} ")
    Demo findById(@Param("id") Integer id) throws Exception;

    /**
     * 使用数据库自增主键
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO demo(CARD_NAME) values(#{demo.cardName})")
    int insert(@Param("demo") Demo demo) throws Exception;

    @Update("UPDATE DEMO SET CARD_NAME = #{demo.cardName} where id = #{demo.id}")
    int update(@Param("demo") Demo demo) throws Exception;

    @Delete("DELETE FROM DEMO WHERE ID = #{id}")
    int delete(@Param("id") Integer id) throws Exception;


}

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

    /*@Select("select * from department where id=#{id}")
    public Department getDeptById(Integer id);

    @Delete("delete from department where id=#{id}")
    public int deleteDeptById(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into department(departmentName) values(#{departmentName})")
    public int insertDept(Department department);

    @Update("update department set departmentName=#{departmentName} where id=#{id}")
    public int updateDept(Department department);*/

    @Select(" SELECT * FROM demo ")
    List<Demo> findPage(DemoDto demoDto) throws Exception;

    @Select(" SELECT * FROM demo WHERE ID = #{id} ")
    Demo findById(@Param("id") Integer id) throws Exception;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into demo(card_name) values(#{demo.cardName})")
    Demo save(@Param("demo") Demo demo);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("update demo set card_name = #{demo.cardName} where id = #{demo.id}")
    Demo update(@Param("demo") Demo demo);


    @Select(" SELECT * FROM demo WHERE CARD_NAME = #{demoDtoVal.cardName} ")
    List<Demo> getDemoListByCondition(@Param("demoDtoVal") DemoDto demoDto) throws Exception;

    List<Demo> getDemoListFast(@Param("demoDto") DemoDto demoDto) throws Exception;

}

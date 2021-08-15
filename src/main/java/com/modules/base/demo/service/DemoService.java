/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.demo.service;

import com.modules.base.demo.model.dto.DemoGetDto;
import com.modules.base.demo.model.entity.Demo;

import java.util.List;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
public interface DemoService {

    Demo findById(Integer id);

    List<Demo> findAll(Integer limitedCount);

    int insert(Demo demo) throws Exception;

    int update(Demo demo) throws Exception;

    int delete(Integer id) throws Exception;

    List<Demo> findPage(DemoGetDto demoGetDto);

    int count(DemoGetDto demoGetDto);

    int batchInsert(List<Demo> demoList) throws Exception;

}

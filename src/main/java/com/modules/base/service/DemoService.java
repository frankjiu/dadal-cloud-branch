/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.service;

import java.util.List;

import com.modules.base.model.dto.DemoDto;
import com.modules.base.model.entity.Demo;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
public interface DemoService {

    List<Demo> findPage(DemoDto demoDto) throws Exception;

    Demo findById(Integer id) throws Exception;

    Demo save(Demo demo) throws Exception;

    List<Demo> findDemoListByCondition(DemoDto demoDto);

}

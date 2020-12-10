/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.service;

import com.modules.base.model.dto.DemoDto;
import com.modules.base.model.entity.Demo;

import java.util.List;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
public interface DemoService {

    List<Demo> findPage(DemoDto demoDto);

    Demo findById(Integer id);

    int save(Demo demo) throws Exception;

    int delete(Integer id) throws Exception;

}

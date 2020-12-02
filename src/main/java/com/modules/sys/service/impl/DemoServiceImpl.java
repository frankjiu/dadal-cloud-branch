/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.sys.service.impl;

import com.modules.sys.dao.DemoDao;
import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import com.modules.sys.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    @Override
    public List<Demo> findDemoList(DemoDto demoDto) throws Exception {
        return demoDao.getDemoList(demoDto);
    }

    @Override
    public List<Demo> findDemoListByCondition(DemoDto demoDto) {
        List<Demo> demoList = new ArrayList<>();
        try {
            demoList = demoDao.getDemoListByCondition(demoDto);
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return demoList;
    }

}

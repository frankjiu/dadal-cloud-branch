/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.service.impl;

import com.modules.base.dao.DemoDao;
import com.modules.base.model.dto.DemoDto;
import com.modules.base.model.entity.Demo;
import com.modules.base.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    @Override
    public List<Demo> findPage(DemoDto demoDto) throws Exception {
        return demoDao.findPage(demoDto);
    }

    @Override
    @Cacheable(value = "DemoCache", key = "#id")
    public Demo findById(Integer id) throws Exception {
        System.out.println(">>>查询数据库...");
        return demoDao.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Demo save(Demo demo) throws Exception {
        if (demo.getId() == null) {
            demo = demoDao.save(demo);
        } else {
            demo = demoDao.update(demo);
        }
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            throw new Exception("save failed!");
        }
        return demo;
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

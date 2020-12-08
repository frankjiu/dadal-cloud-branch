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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    @Cacheable(value = "DemoCache", key = "#id")
    public Demo findDemoById(String id) throws Exception {
        System.out.println(">>>查询数据库...");
        return demoDao.findDemoById(id);
    }

    @Override
    public List<Demo> findDemoList() throws Exception {
        return demoDao.getDemoList();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Demo demo) throws Exception {
        int i = demoDao.insert(demo);
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            throw new Exception("数据插入出错");
        }
        return demo.getId();
    }

}

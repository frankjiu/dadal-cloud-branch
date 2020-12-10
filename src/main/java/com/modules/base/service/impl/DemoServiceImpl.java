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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    @Override
    @Cacheable(value = "DemoCache", key = "#demoDto")
    public List<Demo> findPage(DemoDto demoDto) {
        log.info(">>> Query from database....");
        List<Demo> list = new ArrayList<>();
        try {
            Thread.sleep(3000);
            list = demoDao.findPage(demoDto);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return list;
    }

    @Override
    @Cacheable(value = "DemoCache", key = "#id")
    public Demo findById(Integer id) {
        log.info(">>> Query from database..");
        Demo demo = null;
        try {
            demo = demoDao.findById(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return demo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(Demo demo) throws Exception {
        int num;
        if (demo.getId() == null) {
            num = demoDao.insert(demo);
        } else {
            num = demoDao.update(demo);
        }
        // if (new Random().nextInt(2) == 0) throw new Exception("save failed!"); // 测试事务处理效果
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Integer id) throws Exception {
        return demoDao.delete(id);
    }

}

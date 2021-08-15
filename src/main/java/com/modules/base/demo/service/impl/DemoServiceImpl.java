/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.demo.service.impl;

import com.modules.base.demo.dao.DemoDao;
import com.modules.base.demo.model.dto.DemoGetDto;
import com.modules.base.demo.model.entity.Demo;
import com.modules.base.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Cacheable(value = "DemoCache", key = "#id", cacheManager = "demoCacheManager")
    public Demo findById(Integer id) {
        log.info(">>> Query from database.."); // 测试cache缓存效果
        Demo demo = null;
        try {
            demo = demoDao.findById(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return demo;
    }

    @Override
    public List<Demo> findAll(Integer limitedCount) {
        List<Demo> list = new ArrayList<>();
        try {
            list = demoDao.findAll(limitedCount);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Demo demo) throws Exception {
        int num = demoDao.insert(demo);
        // if (new Random().nextInt(2) == 0) throw new Exception("insert failed!"); // 测试事务处理效果
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "DemoCache", key = "#demo.id")
    public int update(Demo demo) throws Exception {
        return demoDao.update(demo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "DemoCache", key = "#id")
    public int delete(Integer id) throws Exception {
        return demoDao.delete(id);
    }

    @Override
    public List<Demo> findPage(DemoGetDto demoGetDto) {
        List<Demo> list = new ArrayList<>();
        try {
            // Thread.sleep(3000); // 测试并行查询效果
            list = demoDao.findPage(demoGetDto);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public int count(DemoGetDto demoGetDto) {
        int total = 0;
        try {
            total = demoDao.count(demoGetDto);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return total;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Demo> demoList) throws Exception {
        return demoDao.batchInsert(demoList);
    }


}

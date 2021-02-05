package com.function.es.service.impl;

import com.function.es.dao.EsDao;
import com.function.es.model.EsModel;
import com.function.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@Service
public class EsServiceImpl implements EsService {

    @Autowired
    private EsDao esDao;

    @Override
    public void save(EsModel hello) {
        esDao.save(hello);
    }

    @Override
    public void deleteById(long id) {
        esDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        esDao.deleteAll();
    }

    @Override
    public Optional<EsModel> findById(Long id) {
        return esDao.findById(id);
    }

    @Override
    public Iterable<EsModel> findAll() {
        return esDao.findAll();
    }

    @Override
    public Page<EsModel> findAll(Pageable pageable) {
        return esDao.findAll(pageable);
    }

    @Override
    public List<EsModel> findByName(String str) {
        return esDao.findByName(str);
    }

    @Override
    public List<EsModel> findByName(String str, Pageable pageable) {
        return esDao.findByName(str, pageable);
    }


}
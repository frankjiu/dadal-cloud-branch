package com.function.es.service;

import com.function.es.model.EsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
public interface EsService {

    void save(EsModel user);

    void deleteById(long id); //根据文档ID删除

    void deleteAll(); //删除全部

    Optional<EsModel> findById(Long id); //根据文档ID查询数据

    Iterable<EsModel> findAll(); //查询所有数据

    Page<EsModel> findAll(Pageable pageable); //查询所有数据包含分页

    List<EsModel> findByName(String str); //根据Title域中的关键词查找数据

    List<EsModel> findByName(String str, Pageable pageable); //根据Title域中的关键词查找数据

}

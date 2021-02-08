package com.function.es.dao;

import com.function.es.model.EsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@Repository
public interface EsDao extends ElasticsearchRepository<EsModel, Long> {

    List<EsModel> findByName(String name);

    List<EsModel> findByName(String name, Pageable pageable);

}
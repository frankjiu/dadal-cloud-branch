package com.function.es.controller;

import com.function.es.model.EsModel;
import com.function.es.service.EsService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@RestController
@RequestMapping("es")
public class EsController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsService esService;

    @RequestMapping("createIndex")
    public void createIndex() {
        //创建空的索引库
        elasticsearchRestTemplate.indexOps(EsModel.class);
    }

    @RequestMapping("createDocument")
    public void createDocument() {
        for (long i = 1; i <= 30; i++) {
            EsModel esModel = new EsModel();
            esModel.setId(i);
            esModel.setAge(Integer.valueOf(i + ""));
            esModel.setName("frank" + i);
            esModel.setHobby("游游泳" + i);
            esService.save(esModel);
        }
    }

    /**
     * 修改文档数据：先删除再添加，调用的还是save方法
     */
    @RequestMapping("updateDocument")
    public void updateDocument() {
        EsModel esModel = new EsModel();
        esModel.setId(1l);
        esModel.setAge(500);
        esService.save(esModel);
    }

    /**
     * 根据文档ID查询
     */
    @RequestMapping("getDocumentById")
    public void getDocumentById() {
        Optional<EsModel> optional = esService.findById(2l);
        EsModel esModel = optional.get();
        System.out.println(esModel);
    }

    /**
     * 查询所有文档数据
     */
    @RequestMapping("getAllDocument")
    public void getAllDocument() {
        Iterable<EsModel> iterable = esService.findAll();
        iterable.forEach(item -> System.out.println(item));
    }

    /**
     * 查询所有文档数据加分页
     */
    @RequestMapping("getDocumentPage")
    public void getDocumentPage() {
        //指定分页规则
        Page<EsModel> page = esService.findAll(PageRequest.of(0, 5));
        for (EsModel esModel : page.getContent()) {
            System.out.println(esModel);
        }
    }

    /**
     * 根据条件查询文档数据加分页
     */
    @RequestMapping("getDocumentByName")
    public void getDocumentByName() {
        List<EsModel> list = esService.findByName("frank", PageRequest.of(0, 5));
        list.stream().forEach(item -> System.out.println(item));
    }

    /**
     * 根据一段内容查询数据：NativeSearchQuery
     * 先分词再查询
     */
    @RequestMapping("getDocumentQuery")
    public void getDocumentQuery() {
        //创建NativeSearchQueryBuilder对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //指定查询规则
        NativeSearchQuery build = nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery("frank").defaultField("name"))
                .withPageable(PageRequest.of(0, 5)).build();
        //使用ESTemplates执行查询
        // List<EsModel> list = highLevelClient.queryForList(build, EsModel.class);
        List<EsModel> list = elasticsearchRestTemplate.multiGet(build, EsModel.class, IndexCoordinates.of("dadal"));
        list.stream().forEach(item -> System.out.println(item));
    }


}

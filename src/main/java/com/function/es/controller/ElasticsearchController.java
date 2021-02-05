package com.function.es.controller;

import com.function.es.model.EsModel;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@RestController
@RequestMapping("es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @RequestMapping("/save")
    public void save() {
        EsModel esModel = new EsModel();
        esModel.setId(555L);
        esModel.setName("frank");
        esModel.setAge(30);
        esModel.setHobby("music, game, 长跑");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(esModel).build();
        String index = this.elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("dadal"));
        System.out.println(index);
    }

    @RequestMapping("/bulkSave")
    public void bulkSave() {
        List list = new ArrayList();
        for (int i = 0; i < 500; i++) {
            EsModel esModel = new EsModel();
            esModel.setId(1001L + i);
            esModel.setAge(i % 50 + 10);
            esModel.setName("张三" + i);
            esModel.setHobby("足球、篮球、听音乐");
            IndexQuery indexQuery = new IndexQueryBuilder().withObject(esModel).build();
            list.add(indexQuery);
        }
        Long start = System.currentTimeMillis();
        this.elasticsearchRestTemplate.bulkIndex(list, IndexCoordinates.of("dadal"));
        System.out.println("用时：" + (System.currentTimeMillis() - start)); //用时：7836
    }

    /**
     * 局部更新，全部更新使用index覆盖即可
     */
    @RequestMapping("/update")
    public void update() {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source("age", "30");
        UpdateQuery updateQuery = UpdateQuery.builder("555")
                .build();
        this.elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of("dadal"));
    }

    @RequestMapping("/delete")
    public void delete() {
        this.elasticsearchRestTemplate.delete("555", EsModel.class);
    }

    @RequestMapping("/search")
    public void search() {
        PageRequest pageRequest = PageRequest.of(1, 10); //设置分页参数
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "张三")) // match查询
                .withPageable(pageRequest)
                .build();
        //AggregatedPage<EsModel> esModels = elasticsearchRestTemplate.queryForPage(searchQuery, EsModel.class);
        List<EsModel> list = elasticsearchRestTemplate.multiGet(searchQuery, EsModel.class, IndexCoordinates.of("dadal"));
        System.out.println("总页数：" + list.size()); //获取总页数
        for (EsModel esModel : list) { // 获取搜索到的数据
            System.out.println(esModel);
        }
    }

    @RequestMapping("/highlight")
    public void highlight() {
        // 构造条件
        Criteria criteria = Criteria.where(new SimpleField("id"))
                .is("555")
                .and(new SimpleField("name"))
                .contains("frank");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        // 高亮
        HighlightBuilder blogTitle = SearchSourceBuilder.highlight().field("blogTitle");
        HighlightQuery highlightQuery = new HighlightQuery(blogTitle);
        criteriaQuery.setHighlightQuery(highlightQuery);
        SearchHits<EsModel> searchHits = elasticsearchRestTemplate.search(criteriaQuery, EsModel.class);
        searchHits.getSearchHits().forEach(System.out::println);
    }


}

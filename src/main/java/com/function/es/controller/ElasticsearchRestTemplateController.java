package com.function.es.controller;

import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.function.es.model.EsModel;
import com.function.es.model.EsModelHighlightVo;
import com.function.es.model.EsModelVo;
import com.function.es.service.EsService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.IteratorUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@RestController
@Validated
@Slf4j
@RequestMapping("es")
public class ElasticsearchRestTemplateController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsService esService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ES_SYS_INDEX = "dadal"; // 设置索引名称

    private void init() {
        /*boolean exists = elasticsearchRestTemplate.indexOps(EsModel.class).exists();
        if (exists) {
            log.info(">>>>>> Index is already exist!");
            elasticsearchRestTemplate.indexOps(EsModel.class).delete();
        }*/
        elasticsearchRestTemplate.indexOps(EsModel.class).delete();
        elasticsearchRestTemplate.indexOps(EsModel.class).create();
    }

    /**
     * 索引初始化
     * @return
     */
    @PostMapping("/index")
    public HttpResult initIndex() {
        init();
        return HttpResult.success();
    }

    /**
     * 创建文档
     * @param esModel
     * @return
     */
    @PostMapping("/save")
    public HttpResult save(@RequestBody @Valid EsModel esModel) {
        // 方式一: esService.save(esModel); //创建文档
        init();
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(esModel).build();
        String documentId = this.elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(ES_SYS_INDEX));
        return HttpResult.success(documentId);
    }

    @PostMapping("/bulkSave")
    public HttpResult bulkSave(/*@RequestBody @Valid List<EsModel> list*/) {
        List list = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            EsModel esModel = new EsModel();
            esModel.setId(1001L + i);
            esModel.setAge(i % 50 + 10);
            esModel.setName("frank.jiu" + i);
            esModel.setHobby("听歌, 兜风, 冲浪");
            IndexQuery indexQuery = new IndexQueryBuilder().withObject(esModel).build();
            list.add(indexQuery);
        }
        Long start = System.currentTimeMillis();
        List resultList = this.elasticsearchRestTemplate.bulkIndex(list, IndexCoordinates.of(ES_SYS_INDEX));
        log.info("Time cost: " + (System.currentTimeMillis() - start));
        return HttpResult.success(resultList);
    }

    /**
     * 根据文档id删除
     */
    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam("id") String id) {
        // 方式一: esService.deleteById(Long.valueOf(id)); //删除文档
        String deleteId = elasticsearchRestTemplate.delete(id, EsModel.class);
        return HttpResult.success(deleteId);
    }

    /**
     * 根据文档id和条件更新(可分为删除+新增双步骤)
     */
    @PostMapping("/update")
    public HttpResult update(@RequestBody @Valid EsModel esModel) {
        Document document = Document.create();
        document.append("age", esModel.getAge());
        UpdateQuery updateQuery = UpdateQuery.builder(esModel.getId().toString()).withDocument(document).build();
        UpdateResponse update = this.elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of(ES_SYS_INDEX));
        return HttpResult.success(update.getResult());
    }

    /**
     * 根据文档ID查询
     */
    @RequestMapping("findById")
    public HttpResult findById() {
        Optional<EsModel> optional = esService.findById(2l);
        EsModel esModel = optional.get();
        return HttpResult.success(esModel);
    }

    /**
     * 查询所有文档数据
     */
    @RequestMapping("findAll")
    public HttpResult findAll() {
        Iterable<EsModel> iterable = esService.findAll();
        List<EsModel> list = IteratorUtils.toList(iterable.iterator());
        return HttpResult.success(list);
    }

    /**
     * 查询文档分页数据
     */
    @RequestMapping("findPage")
    public HttpResult findPage() {
        Page<EsModel> page = esService.findAll(PageRequest.of(1, 10));
        List<EsModel> pageData = page.getContent();
        long totalCount = page.getTotalElements();
        PageModel<EsModel> pageModel = new PageModel<>(1, 10, totalCount);
        pageModel.setData(pageData);
        return HttpResult.success(pageModel);
    }

    /**
     * 条件查询文档分页数据
     */
    @PostMapping("/searchPage")
    public HttpResult searchPage(@RequestBody @Valid EsModelVo esModelVo) {
        PageRequest pageRequest = PageRequest.of(esModelVo.getPageNum(), esModelVo.getPageSize());
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", esModelVo.getName()))
                .withPageable(pageRequest)
                .build();
        SearchHits<EsModel> searchResponse = elasticsearchRestTemplate.search(searchQuery, EsModel.class, IndexCoordinates.of(ES_SYS_INDEX));
        List<SearchHit<EsModel>> searchHits = searchResponse.getSearchHits();
        long totalCount = searchResponse.getTotalHits();
        List<EsModel> pageData = Lists.newArrayList();
        for (SearchHit<EsModel> searchHit : searchHits) {
            EsModel esModel = searchHit.getContent();
            pageData.add(esModel);
        }
        // 分页 long pageNum, long pageSize, long totalCount
        PageModel<EsModel> pageModel = new PageModel<>(esModelVo.getPageNum(), esModelVo.getPageSize(), totalCount);
        pageModel.setData(pageData);
        return HttpResult.success(pageModel);
    }

    /**
     * 高亮显示数据
     */
    @PostMapping("/highlight")
    public HttpResult highlight(@RequestBody @Valid EsModelHighlightVo vo) {
        // 构建Criteria
        Criteria criteria = Criteria
                .where(new SimpleField("id")).is(vo.getId())
                .and(new SimpleField("name")).contains(vo.getName());
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        // 高亮
        HighlightBuilder highlightField = SearchSourceBuilder.highlight().field("name");
        HighlightQuery highlightQuery = new HighlightQuery(highlightField);
        criteriaQuery.setHighlightQuery(highlightQuery);
        SearchHits<EsModel> searchHits = elasticsearchRestTemplate.search(criteriaQuery, EsModel.class);
        return HttpResult.success(searchHits.getSearchHits());
    }

}

package com.function.es.controller;

import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.function.es.model.EsModel;
import com.function.es.model.EsModelVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: recommended by the official
 * @Author: QiuQiang
 * @Date: 2021-02-08
 */
@RestController
@Validated
@Slf4j
@RequestMapping("es")
public class ElasticsearchRestHighLevelController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static String ES_SYS_INDEX = "dadal";

    private static String ES_SYS_KEYWORD = "frank";

    private static String ES_EVENT_ORDER = "desc";

    private static int REST_STATUS_200 = 200;

    /**
     * 判断索引是否存在
     */
    @PostMapping("exists")
    public boolean existsIndex(@RequestParam("indexName") String indexName) throws IOException {
        GetIndexRequest getRequest = new GetIndexRequest(indexName);
        getRequest.local(false);
        getRequest.humanReadable(true);
        return restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     */
    @PostMapping("createIndex")
    public void createIndex(@RequestParam("indexName") String indexName) throws IOException {
        if (existsIndex(indexName)) {
            log.info("Index is already exist!");
            return;
        }
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        //配置文件
        ClassPathResource seResource = new ClassPathResource("mapper/setting.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(seInputStream, "UTF-8"));
        seInputStream.close();
        //映射文件
        ClassPathResource mpResource = new ClassPathResource("mapper/" + indexName + "-mapping.json");
        InputStream mpInputStream = mpResource.getInputStream();
        String mpJson = String.join("\n", IOUtils.readLines(mpInputStream, "UTF-8"));
        mpInputStream.close();
        request.settings(seJson, XContentType.JSON);
        request.mapping(mpJson, XContentType.JSON);
        //设置别名
        request.alias(new Alias(indexName + "_alias"));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if (falg) {
            log.info("创建索引库: {} 创建成功!", indexName);
        }
    }

    /**
     * 删除索引
     */
    @PostMapping("deleteIndex")
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse deleteIndexResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        return deleteIndexResponse.isAcknowledged();
    }

    /**
     * 保存文档json映射里面键值对，index是索引名称
     */
    @PostMapping("saveDocument")
    public boolean save(EsModelVo vo) throws IOException {
        IndexRequest request = new IndexRequest(vo.getIndex())
                .id(vo.getId()).source(vo);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return response.isFragment();
    }

    /**
     * 根据id删除文档
     */
    @PostMapping("deleteById")
    public boolean deleteById(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(ES_SYS_INDEX.toLowerCase(), id);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return response.isFragment();
    }

    /**
     * 分页分词关键词查询
     * 使用QueryBuilder
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     * 组合查询
     * must(QueryBuilders) :   AND
     * mustNot(QueryBuilders): NOT
     * should:                  : OR
     * percent_terms_to_match：匹配项（term）的百分比，默认是0.3
     * min_term_freq：一篇文档中一个词语至少出现次数，小于这个值的词将被忽略，默认是2
     * max_query_terms：一条查询语句中允许最多查询词语的个数，默认是25
     * stop_words：设置停止词，匹配时会忽略停止词
     * min_doc_freq：一个词语最少在多少篇文档中出现，小于这个值的词会将被忽略，默认是无限制
     * max_doc_freq：一个词语最多在多少篇文档中出现，大于这个值的词会将被忽略，默认是无限制
     * min_word_len：最小的词语长度，默认是0
     * max_word_len：最多的词语长度，默认无限制
     * boost_terms：设置词语权重，默认是1
     * boost：设置查询权重，默认是1
     * analyzer：设置使用的分词器，默认是使用该字段指定的分词器
     */
    @PostMapping("findPaging")
    public HttpResult findPage(EsModelVo vo) {
        PageModel<EsModel> page = null;
        try {
            // 构建查询
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 索引查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //boost 设置权重
            //分词查询
            boolQueryBuilder.should(QueryBuilders.matchQuery("keyword", vo.getName()).boost(2f));
            //拼音查询
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("keyword.pinyin", vo.getName()).boost(2f));
            //模糊查询，不区分大小写
            // boolQueryBuilder.should(QueryBuilders.wildcardQuery("keyword", "*"+vo.getKeyword().toLowerCase()+"*").boost(2f));
            //指定商家的性质
            if (org.apache.commons.lang3.StringUtils.isNotBlank(vo.getName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("ownerNature", vo.getName()));
            }
            //必须满足should其中一个条件
            boolQueryBuilder.minimumShouldMatch(1);
            //时间范围查询
            // boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime")
            //          .from(DateKit.format(DateKit.getDayBegin(),"yyyy-MM-dd HH:mm:ss"))
            //          .to(DateKit.format(DateKit.getDayBegin(),"yyyy-MM-dd HH:mm:ss")));
            sourceBuilder.query(boolQueryBuilder);
            // 设置返回的字段
            // String[] includeFields = new String[] {"keyword"};
            // sourceBuilder.fetchSource(includeFields,null);
            // 分页设置
            sourceBuilder.from(vo.getPageNum());
            sourceBuilder.size(vo.getPageSize());
            sourceBuilder.sort("id", SortOrder.ASC); // 设置排序规则
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            SearchRequest searchRequest = new SearchRequest(vo.getIndex());
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            long totalCount = searchHits.getTotalHits().value;
            List<EsModel> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                EsModel esModel = new EsModel();
                esModel.setId(1001L + i);
                esModel.setAge(i % 5 + 10);
                esModel.setName("frank.jiu" + i);
                esModel.setHobby("听歌, 兜风, 冲浪");
                list.add(esModel);
            }
            page = new PageModel<>(vo.getPageNum(), vo.getPageSize(), totalCount);
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //收集关键词搜索记录
        vo.setIndex(ES_SYS_KEYWORD.toLowerCase());
        // ...
        return HttpResult.success(page);
    }

    /**
     * 批量保存
     *
     * @param elasticDTOList
     * @return
     * @throws JsonProcessingException
     */
    public boolean createBatch(List<EsModelVo> elasticDTOList) throws JsonProcessingException {
        if (elasticDTOList == null || elasticDTOList.size() == 0) {
            return true;
        }
        //将id取出放入list
        List<String> ids = new ArrayList<>(elasticDTOList.size());
        //创建map存放id和对应数据
        Map<String, String> idJsonParamMap = new HashMap<>(elasticDTOList.size());
        for (EsModelVo elasticDTO : elasticDTOList) {
            String idStr = elasticDTO.getId().toString();
            ids.add(idStr);
            // String jsonParam = JSONObject.toJSONString(elasticDTO, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            String jsonParam = objectMapper.writeValueAsString(elasticDTO);
            idJsonParamMap.put(idStr, jsonParam);
        }
        return createBatch(ES_SYS_INDEX, ES_EVENT_ORDER, ids, idJsonParamMap);
    }

    /**
     * 批量保存: 传入索引、类型、id的list列表和<id json字符串数据>map进行新增操作
     *
     * @param index
     * @param type
     * @param ids
     * @param idJsonParamMap
     * @return
     */
    public boolean createBatch(String index, String type, List<String> ids, Map<String, String> idJsonParamMap) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        //创建BulkRequest
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1m");
        //便利id的list,插入到BulkRequest中。
        for (String id : ids) {
            IndexRequest indexRequest = new IndexRequest(index, type, id).source(idJsonParamMap.get(id), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            //执行插入请求操作
            //BulkResponse bulkResponse = new BulkResponse(null);
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.status().getStatus() == REST_STATUS_200) {
                return true;
            }
        } catch (IOException e) {
            throw new CommonException("restClient.createBatch.error");
        }
        return false;
    }


}

package com.function.es.service.impl;

import com.function.es.dao.EsDao;
import com.function.es.model.EsModel;
import com.function.es.service.EsService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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


    /////////////////////////////////

    public void findList() {
        SearchRequest searchRequest = new SearchRequest(); //SearchRequest searchRequest = new SearchRequest("posts");
        searchRequest.routing("routing");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); //parameters are added
        // searchSourceBuilder...
        sourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        sourceBuilder.query(QueryBuilders.matchAllQuery()); //Add a match_all query to the SearchSourceBuilder.

        searchRequest.indices("posts");
        searchRequest.source(sourceBuilder); //Add the SearchSourceBuilder to the SearchRequest.


        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "kimchy");
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        matchQueryBuilder.prefixLength(3);
        matchQueryBuilder.maxExpansions(10);

        matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);

        sourceBuilder.query(matchQueryBuilder);

        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));

        String[] includeFields = new String[] {"title", "innerObject.*"};
        String[] excludeFields = new String[] {"user"};
        sourceBuilder.fetchSource(includeFields, excludeFields);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title");
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
        highlightBuilder.field(highlightUser);
        sourceBuilder.highlighter(highlightBuilder);

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
                .field("company.keyword");
        aggregation.subAggregation(AggregationBuilders.avg("average_age")
                .field("age"));
        sourceBuilder.aggregation(aggregation);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.profile(true);


    }



}
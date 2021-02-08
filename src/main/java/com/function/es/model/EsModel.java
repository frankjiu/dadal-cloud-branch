package com.function.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "dadal", shards = 6, replicas = 1, createIndex = true)
public class EsModel {

    @Id
    private Long id;

    @Field(store = true)
    private String name;

    @Field
    private Integer age;

    @Field
    private String hobby;

}

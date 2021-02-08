package com.function.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.time.Duration;

/**
 * @Description: Es RestClient Config(springboot已自动配置, 无需注入)
 * @Author: QiuQiang
 * @Date: 2021-01-05
 */
/*@Configuration
public class EsRestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${es.host.port}")
    private String hostAndPort;
    @Value("${es.username}")
    private String username;
    @Value("${es.password}")
    private String password;
    @Value("${es.socketTimeout}")
    private long socketTimeout;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .withBasicAuth(username, password)
                .withSocketTimeout(Duration.ofSeconds(socketTimeout))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate restTemplate() throws Exception {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }


}*/


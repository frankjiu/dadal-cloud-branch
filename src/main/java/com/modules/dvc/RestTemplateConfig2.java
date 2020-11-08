package com.modules.dvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: RestTemplate Config类
 * @Author: QiuQiang
 * @Date: 2020-11-07
 */

/*@Configuration
public class RestTemplateConfig2 {

    // 配置 RestTemplate
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        // 创建一个 httpCilent 简单工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
        factory.setConnectTimeout(15000);
        // 设置读取超时
        factory.setReadTimeout(5000);
        return factory;
    }
}*/

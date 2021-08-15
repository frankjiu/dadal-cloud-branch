package com.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * @Description: ForkJoinPool 线程池配置
 * @Author: QiuQiang
 * @Date: 2020-05-10
 */
@Configuration
public class ForkJoinPoolConf {

    @Bean
    public ForkJoinPool pool() {
        return new ForkJoinPool(100);
    }


}

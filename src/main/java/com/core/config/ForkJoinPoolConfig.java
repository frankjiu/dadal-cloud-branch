package com.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-05-10
 */
@Configuration
public class ForkJoinPoolConfig {

    @Bean
    public ForkJoinPool pool() {
        return new ForkJoinPool(100);
    }


}

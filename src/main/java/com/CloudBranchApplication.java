/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date:   2020年4月18日下午8:49:33
 * @version V1.0
 */

package com;

import com.core.filter.CommonFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;

/**
 * Cloud Branch Application
 * 
 * @author: Frankjiu
 * @date: 2020年4月18日 下午8:49:33
 */
@SpringBootApplication
@EnableCaching
@PropertySource("classpath:config.properties") // 加载config.properties文件
public class CloudBranchApplication {

    /**
     * 注册 CommonFilter 过滤器
     */
    @Bean
    public FilterRegistrationBean filterRegist() {
        FilterRegistrationBean<Filter> filterRegister = new FilterRegistrationBean<>();
        // 创建并注册 CommonFilter
        filterRegister.setFilter(new CommonFilter());
        // 对所有请求进行拦截
        filterRegister.addUrlPatterns("/*");
        // 设置过滤器名字
        filterRegister.setName("CommonFilter");
        // 设置过滤器执行顺序, 越小优先
        filterRegister.setOrder(1);
        return filterRegister;
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudBranchApplication.class, args);
    }

}

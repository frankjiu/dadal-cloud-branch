/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date:   2020年4月18日下午8:49:33
 * @version V1.0
 */

package com;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.core.filter.CommonFilter;
import com.modules.sys.admin.config.PermitionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Branch Application
 * 
 * @author: Frankjiu
 * @date: 2020年4月18日 下午8:49:33
 */
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
public class CloudBranchApplication {

    /**
     * 注册 CommonFilter 过滤器
     */
    @Bean
    public FilterRegistrationBean filterRegist() {
        FilterRegistrationBean<Filter> filterRegister = new FilterRegistrationBean<>();
        // 创建并注册 CommonFilter
        filterRegister.setFilter(new CommonFilter());
        filterRegister.setFilter(new PermitionFilter());
        // 对所有请求进行拦截
        filterRegister.addUrlPatterns("/*");
        // 设置过滤器名字
        filterRegister.setName("CommonFilter");
        // 设置过滤器执行顺序, 越小优先
        filterRegister.setOrder(1);
        return filterRegister;
    }

    /**
     * 初始化DruidDataSource 对象
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druid() {
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }

    // 注册ServletBean,进行后台管理(路径/druid)
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> param = new HashMap<>();
        param.put("loginUsername", "admin");
        param.put("loginPassword", "123456");
        param.put("allow", ""); // 设置允许访问后台的IP, 空字符代表所有地址
        param.put("deny", "192.168.5.68"); // 设置禁止访问后台的IP
        bean.setInitParameters(param);
        return bean;
    }

    // 监听获取应用数据, Filter用于收集数据, Servlet用于展现数据
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter()); //设置过滤器
        bean.addUrlPatterns("/*");
        Map<String, String> param = new HashMap<>();
        // 排除静态资源
        param.put("exclusions", "*.js,*.css,*.png,*.woff,/druid/*");
        bean.setInitParameters(param);
        return bean;
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudBranchApplication.class, args);
    }

}

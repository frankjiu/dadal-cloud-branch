package com.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description: application param
 * @Author: QiuQiang
 * @Date: 2020-12-18
 */
@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "app") // 将config.properties文件中所有app前缀的属性自动赋值给该Bean对应属性.
@Data
public class AppConfig {

    private String name;
    private Integer age;
    private Boolean isTall;

    private String website;
    private String reportName;

}

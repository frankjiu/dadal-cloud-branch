package com.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-11
 */
@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {

    /*private String name;
    private Integer age;
    private boolean isTall;

    private String website;
    private String reportName;*/

}

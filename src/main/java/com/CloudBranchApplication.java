/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date:   2020年4月18日下午8:49:33
 * @version V1.0
 */

package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

/**
 * Cloud Branch Application
 * 
 * @author: Frankjiu
 * @date: 2020年4月18日 下午8:49:33
 */
@SpringBootApplication
@EnableCaching
//加载config.properties文件
@PropertySource("classpath:config.properties")
public class CloudBranchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudBranchApplication.class, args);
    }

}

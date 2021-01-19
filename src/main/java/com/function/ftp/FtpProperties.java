package com.function.ftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-18
 */
@Data
@ConfigurationProperties(prefix = "config.ftp")
public class FtpProperties {

    private String url;
    private Integer port;
    private String username;
    private String password;
    private String filedir;
    private String encode;
    private Boolean isRunner;
    private String mode;
    private String cron;
    private Integer retryCount;

}
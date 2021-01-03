package com.modules.sys.admin.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
@Configuration
public class MappingExceptionResolver {

    /**
     * 访问地址不存在或无权限时配置
     *
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorized.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
            factory.addErrorPages(error401Page, error404Page, error500Page);
        };
    }

}

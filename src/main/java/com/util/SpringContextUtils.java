package com.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Spring Bean Utils
 * @Author: QiuQiang
 * @Date: 2020-06-05
 */
@Configuration
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBeanByName(String className) {
        return context.getBean(className);
    }

}
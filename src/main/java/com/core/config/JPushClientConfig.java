package com.core.config;

import cn.jpush.api.JPushClient;
import com.core.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-23
 */
@Configuration
public class JPushClientConfig {

    @Bean
    public JPushClient jPushClient() {
        return new JPushClient(Constant.JiGuang.MASTER_SECRET, Constant.JiGuang.APP_KEY);
    }

}

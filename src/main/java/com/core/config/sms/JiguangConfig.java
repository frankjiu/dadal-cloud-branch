package com.core.config.sms;

import cn.jsms.api.common.SMSClient;
import com.core.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-22
 */
@Configuration
public class JiguangConfig {

    @Bean(name = "AppSMSClient")
    public SMSClient smsClient() {
        return new SMSClient(Constant.JiGuang.MASTER_SECRET, Constant.JiGuang.APP_KEY);
    }

}

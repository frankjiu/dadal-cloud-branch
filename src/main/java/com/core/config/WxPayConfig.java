package com.core.config;

import com.core.constant.Constant;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方wx支付包配置
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        com.github.binarywang.wxpay.config.WxPayConfig payConfig = new com.github.binarywang.wxpay.config.WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(Constant.WxPay.APPID));
        payConfig.setMchId(StringUtils.trimToNull(Constant.WxPay.MCH_ID));
        payConfig.setMchKey(StringUtils.trimToNull(Constant.WxPay.KEY));
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}

package com.core.utils;

import com.github.wxpay.sdk.WXPayConfig;
import com.wlhlwl.hyzc.customer.core.constant.Constant;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description:
 * @date: 2021/6/11/0011 14:56
 * @author: YuZHenBo
 */
public class WXConfigUtil implements WXPayConfig {

    private byte[] certData;

    public WXConfigUtil() throws Exception {

        // TODO 从微信商户平台下载新的安全证书
        String certPath = "";//从微信商户平台下载的安全证书存放的路径
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return Constant.WxPay.APPID;
    }

    @Override
    public String getMchID() {
        return Constant.WxPay.MCH_ID;
    }

    @Override
    public String getKey() {
        return Constant.WxPay.KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}

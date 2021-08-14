package com.core.utils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wlhlwl.hyzc.customer.core.constant.Constant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @date: 2021/5/22/0022 10:59
 * @author: YuZHenBo
 */
@Component
public class AliPayUtil {

    private volatile AlipayClient alipayClient;
    /**
     * 得到ali访问对象
     * @return
     */
    private AlipayClient getAlipayClient() {
        if(alipayClient == null) {
            synchronized (this) {
                if(alipayClient == null) {
                    alipayClient = new DefaultAlipayClient(Constant.ALiPay.URL, Constant.ALiPay.APP_ID,
                            Constant.ALiPay.APP_PRIVATE_KEY, Constant.ALiPay.FORMAT, Constant.ALiPay.CHARSET,
                            Constant.ALiPay.ALIPAY_PUBLIC_KEY,
                            Constant.ALiPay.SIGN_TYPE);
                }
            }
        }
        return alipayClient;
    }

    /**
     * 授权登录用授权码获取用户令牌 (auth_code -> auth_token)
     * @param authCode
     * @return
     * @throws AlipayApiException
     */
    public AlipaySystemOauthTokenResponse getAuthToken(String authCode) throws AlipayApiException {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);
        return getAlipayClient().execute(request);
    }

    /**
     * 用用户令牌获取用户信息
     * @param authToken
     * @return
     * @throws AlipayApiException
     */
    public AlipayUserInfoShareResponse getUserInfo(String authToken) throws AlipayApiException {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        return getAlipayClient().execute(request, authToken);
    }

    /**
     * 支付宝扫码支付
     * @param tradeNo
     * @param sellerID
     * @param totalAmount
     * @param subject
     * @param operatorID
     * @param storeID
     * @param notifyURL
     * @param authToken
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradePrecreateResponse tradePrecreate(String tradeNo,
                                                       String sellerID,
                                                       String totalAmount,
                                                       String subject,
                                                       String operatorID,
                                                       String storeID,
                                                       String notifyURL,
                                                       String authToken) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", tradeNo);
        params.put("seller_id", sellerID);
        params.put("total_amount", totalAmount);
        params.put("subject", subject);
        params.put("operator_id", operatorID);
        if(storeID != null) {
            params.put("store_id", storeID);
        }
        params.put("qr_code_timeout_express",Constant.ALiPay.QRCODE_TIMEOUT);
        request.putOtherTextParam("app_auth_token", authToken);
        if(notifyURL != null) {
            request.setNotifyUrl(notifyURL);
        }
        request.setBizContent(JSON.toJSONString(params));
        return getAlipayClient().execute(request);
    }

    /**
     * 支付宝扫码支付
     * @param tradeNo
     * @param totalAmount
     * @param subject
     * @param notifyUrl
     * @param authToken
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradePrecreateResponse tradePrecreate(String tradeNo,
                                                       String totalAmount,
                                                       String subject,
                                                       String notifyUrl,
                                                       String authToken) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", tradeNo);
        params.put("total_amount", totalAmount);
        params.put("subject", subject);
        params.put("qr_code_timeout_express",Constant.ALiPay.QRCODE_TIMEOUT);
        request.putOtherTextParam("app_auth_token", authToken);
        request.setNotifyUrl(notifyUrl);
        request.setBizContent(JSON.toJSONString(params));
        return getAlipayClient().execute(request);
    }


    /**
     * 支付宝条码支付接口
     * @param tradeNo
     * @param totalAmount
     * @param subject
     * @param authToken
     * @param authCode
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradePayResponse scanPay(String tradeNo,
                                          String totalAmount,
                                          String subject,
                                          String authToken,
                                          String authCode) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", tradeNo);
        params.put("scene", "bar_code");
        params.put("auth_code", authCode);
        params.put("subject", subject);
        params.put("total_amount", totalAmount);
        //app_auth_token是三方应用授权的授权token,如果不是三方授权的应用而只是自研小程序，无需这个值
        request.putOtherTextParam("app_auth_token", authToken);
        request.setBizContent(JSON.toJSONString(params));
        return getAlipayClient().execute(request);
    }

    /**
     * 支付宝app支付
     * @param tradeNo
     * @param subject
     * @param totalAmount
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradeAppPayResponse appPay(String tradeNo, String subject, String totalAmount) throws AlipayApiException {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        Map<String, String > param = new HashMap<>();
        param.put("out_trade_no", tradeNo);
        param.put("subject", subject);
//        param.put("total_amount", totalAmount);
        //TODO 测试环境0.01元
        param.put("total_amount", "0.01");
        param.put("product_code", "QUICK_MSECURITY_PAY");
        request.setBizContent(JSON.toJSONString(param));
        request.setNotifyUrl(Constant.ALiPay.APP_NOTIFY_URL);
        return getAlipayClient().sdkExecute(request);
    }

    /**
     * 查询支付宝订单
     * @param orderID
     * @param auth
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradeQueryResponse query(String orderID, String auth) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderID);
        request.putOtherTextParam("app_auth_token", auth);
        request.setBizContent(JSON.toJSONString(params));
        return getAlipayClient().execute(request);
    }

    /**
     * 关闭支付宝订单
     * @param orderID
     * @param operatorID
     * @param auth
     * @return
     * @throws AlipayApiException
     */
    public AlipayTradeCloseResponse close(String orderID, String operatorID, String auth) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderID);
        params.put("operator_id", operatorID);
        request.putOtherTextParam("app_auth_token", auth);
        request.setBizContent(JSON.toJSONString(params));
        return getAlipayClient().execute(request);
    }

}

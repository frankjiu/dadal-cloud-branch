package com.modules.login.service.impl;

import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.AliPayUtil;
import com.modules.login.model.vo.AccessTokenVo;
import com.modules.login.model.vo.AliUserInfoVo;
import com.modules.login.model.vo.WxParamVo;
import com.modules.login.service.AliLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * @Description:
 * @date: 2021/5/21/0021 17:10
 * @author: YuZHenBo
 */
@Service
@Slf4j
public class AliLoginServiceImpl implements AliLoginService {

    @Autowired
    private AliPayUtil aliPayUtil;

    @Override
    public HttpResult getSign(String userId) throws Exception {
        String sign = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("apiname=com.alipay.account.auth");
        stringBuffer.append("&app_id=");
        stringBuffer.append(Constant.ALiPay.APP_ID);
        stringBuffer.append("&app_name=mc");
        stringBuffer.append("&auth_type=AUTHACCOUNT");
        stringBuffer.append("&biz_type=openservice");
        stringBuffer.append("&method=alipay.open.auth.sdk.code.get");
        stringBuffer.append("&pid=");
        stringBuffer.append(Constant.ALiPay.PID);
        stringBuffer.append("&product_id=APP_FAST_LOGIN");
        stringBuffer.append("&scope=kuaijie");
        stringBuffer.append("&sign_type=RSA2");
        stringBuffer.append("&target_id=");
        stringBuffer.append(userId);
        sign = AlipaySignature.rsaSign(stringBuffer.toString(), Constant.ALiPay.APP_PRIVATE_KEY,
                Constant.ALiPay.CHARSET, Constant.ALiPay.SIGN_TYPE);
        stringBuffer.append("&sign=" + URLEncoder.encode(sign, "UTF-8"));
        WxParamVo result = new WxParamVo();
        result.setAuth(stringBuffer.toString());
        return HttpResult.success(result);
    }

    @Override
    public AccessTokenVo getAuthToken(String authCode) throws Exception {
        AlipaySystemOauthTokenResponse response = aliPayUtil.getAuthToken(authCode);
        AccessTokenVo result = new AccessTokenVo();
        if (response.isSuccess()) {
            result.setAccessToken(response.getAccessToken());
            result.setOpenId(response.getUserId());
            return result;
        } else {
            throw new CommonException(RespCode.GET_TOKEN_FAILED.getDescription());
        }
    }

    @Override
    public AliUserInfoVo getUserInfo(String authToken) throws Exception {
        AlipayUserInfoShareResponse response = aliPayUtil.getUserInfo(authToken);
        if (response.isSuccess()) {
            AliUserInfoVo userInfo = new AliUserInfoVo();
            BeanUtils.copyProperties(userInfo, response);
            userInfo.setToken(authToken);
            userInfo.setUsername(response.getUserName());
            userInfo.setNickname(response.getNickName());
            userInfo.setBirthday(response.getPersonBirthday());
            userInfo.setHeadimgurl(response.getAvatar());
            userInfo.setCountry(response.getCountryCode());
            return userInfo;
        } else {
            log.error(">>>>>>>>>>>>>>>获取支付宝用户信息发生错误："+ response.getSubMsg());
            throw new CommonException(response.getSubMsg());
        }
    }
}

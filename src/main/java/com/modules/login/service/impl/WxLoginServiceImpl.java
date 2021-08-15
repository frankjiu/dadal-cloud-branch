package com.modules.login.service.impl;

import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.RespCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modules.login.model.vo.AccessTokenVo;
import com.modules.login.model.vo.WxErrInfo;
import com.modules.login.model.vo.WxUserInfo;
import com.modules.login.model.vo.WxUserInfoVo;
import com.modules.login.service.WxLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-16
 */
@Service
@Slf4j
public class WxLoginServiceImpl implements WxLoginService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    /**
     * 微信授权登录认证: 通过code获取access_token
     *
     * @param code 授权码
     * @return
     */
    @Override
    public AccessTokenVo getAccessToken(String code) throws Exception {
        String url = Constant.WxPay.ACCESS_TOKEN_URL + "?appid="
                + Constant.WxPay.APPID
                + "&secret=" + Constant.WxPay.SECRET
                + "&code=" + code
                + "&grant_type=" + Constant.WxPay.AUTHORIZATION_CODE_BASE;

        String jsonData = restTemplate.getForObject(url, String.class);
        if (org.apache.commons.lang3.StringUtils.contains(jsonData, "errcode")) {
            WxErrInfo wxErrInfo = mapper.readValue(jsonData, new TypeReference<WxErrInfo>() {
            });
            if ("40163".equals(wxErrInfo.getErrcode())) {
                log.error(RespCode.CODE_BEEN_USED.getDescription());
                throw new CommonException(RespCode.CODE_BEEN_USED.getDescription());
            }
            if (!StringUtils.isEmpty(wxErrInfo.getErrcode())) {
                log.error(RespCode.WX_LOGIN_FAILED.getDescription());
                throw new CommonException(RespCode.WX_LOGIN_FAILED.getDescription());
            }
            log.error(RespCode.WX_LOGIN_FAILED.getDescription());
            throw new CommonException(RespCode.WX_LOGIN_FAILED.getDescription());
        }

        WxUserInfo wxUserInfo = mapper.readValue(jsonData, new TypeReference<WxUserInfo>() {
        });
        String accessToken = wxUserInfo.getAccess_token();
        String openId = wxUserInfo.getOpenid();
        AccessTokenVo accessTokenVo = new AccessTokenVo();
        accessTokenVo.setAccessToken(accessToken);
        accessTokenVo.setOpenId(openId);
        return accessTokenVo;
    }

    /**
     * @param vo accessToken 接口调用凭证
     * @param vo openID      授权用户唯一标识
     * @return
     */
    @Override
    public WxUserInfoVo getUserInfo(AccessTokenVo vo) throws Exception {
        if (isAccessTokenIsValid(vo.getAccessToken(), vo.getOpenId()) && System.currentTimeMillis() / 1000 < vo.getBindTime() + Long.valueOf(Constant.WxPay.EXPIRES_TIME)) {
            String url = Constant.WxPay.USER_INFO_URL
                    + "?access_token=" + vo.getAccessToken()
                    + "&openid=" + vo.getOpenId()
                    + "&grant_type=" + Constant.WxPay.AUTHORIZATION_CODE_USERINFO;
            String jsonData = restTemplate.getForObject(url, String.class);
            if (org.apache.commons.lang3.StringUtils.contains(jsonData, "errcode")) {
                WxErrInfo wxErrInfo = mapper.readValue(jsonData, new TypeReference<WxErrInfo>() {
                });
                if (!StringUtils.isEmpty(wxErrInfo.getErrcode())) {
                    log.error(">>>>>>>>>>>>>>>>>>>用户微信信息[获取失败]!");
                    return null;
                }
            }
            WxUserInfoVo userInfo = mapper.readValue(jsonData, new TypeReference<WxUserInfoVo>() {
            });
            return userInfo;
        }
        log.error(">>>>>>>>>>>>>>>>>>>获取用户微信信息[access_token失效 或 已过期]!");
        return null;
    }

    /**
     * 校验微信认证的access_token是否有效
     *
     * @param accessToken
     * @param openID
     * @return
     */
    @Override
    public boolean isAccessTokenIsValid(String accessToken, String openID) throws Exception {
        String url = Constant.WxPay.CHECK_TOKEN_URL + "?access_token=" + accessToken + "&openid=" + openID;
        String jsonData = restTemplate.getForObject(url, String.class);
        if (org.apache.commons.lang3.StringUtils.contains(jsonData, "errcode")) {
            WxErrInfo wxErrInfo = mapper.readValue(jsonData, new TypeReference<WxErrInfo>() {
            });
            // 有效
            if ("0".equals(wxErrInfo.getErrcode())) {
                return true;
            }
            if ("40003".equals(wxErrInfo.getErrcode())) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String refreshAccessToken(String appId, String refreshToken) {
        String uri = Constant.WxPay.REFRESH_TOKEN_URL + "?appid=" + appId
                + "&grant_type=refresh_token&refresh_token=" + refreshToken;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(URI.create(uri));
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder stb = new StringBuilder();
                for (String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
                    stb.append(ln);
                }
                JSONObject js = new JSONObject(stb.toString().trim());
                refreshToken = js.getString("refresh_token");
                return refreshToken;
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>调用微信接口刷新accessToken失败:" + e.getMessage(), e);
            throw new CommonException(RespCode.API_ERROR.getDescription());
        }
        return refreshToken;
    }


}

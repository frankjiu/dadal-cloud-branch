package com.modules.login.service;

import com.modules.login.model.vo.AccessTokenVo;
import com.modules.login.model.vo.WxUserInfoVo;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021/6/16 10:35
 */
public interface WxLoginService {

    AccessTokenVo getAccessToken(String code) throws Exception;

    WxUserInfoVo getUserInfo(AccessTokenVo vo) throws Exception;

    boolean isAccessTokenIsValid(String accessToken, String openID) throws Exception;

    String refreshAccessToken(String appId, String refreshToken);

}

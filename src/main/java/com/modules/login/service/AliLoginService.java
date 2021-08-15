package com.modules.login.service;

import com.core.result.HttpResult;
import com.modules.login.model.vo.AccessTokenVo;
import com.modules.login.model.vo.AliUserInfoVo;
import com.modules.login.model.vo.WxParamVo;
import com.modules.login.model.vo.WxUserInfoVo;

/**
 * @Description:
 * @date: 2021/5/21/0021 17:10
 * @author: YuZHenBo
 */
public interface AliLoginService {

    HttpResult<WxParamVo> getSign(String userId) throws Exception;

    AccessTokenVo getAuthToken(String authCode) throws Exception;

    AliUserInfoVo getUserInfo(String authToken) throws Exception;
}

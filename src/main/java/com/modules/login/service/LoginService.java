package com.modules.login.service;

import com.core.result.HttpResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.modules.gen.model.entity.User;
import com.modules.login.model.dto.*;
import com.modules.login.model.vo.*;

/**
 * @Description:
 * @date: 2021/5/17/0017 10:32
 * @author: YuZHenBo
 */
public interface LoginService {

    String login(User user) throws JsonProcessingException, Exception;

    void update(User user) throws Exception;

    User find(String openId, int type) throws Exception;

    User find(String mobile);

    User findById(Long uid) throws Exception;

    void logout() throws Exception;

    User insert(User user, BindPhoneDto dto) throws Exception;

    User insert(User user, String mobile) throws Exception;

    PhoneLoginVo phoneLogin(String mobile) throws Exception;

    NeedOtherVo needOther() throws Exception;

    HttpResult bindOther(BindOtherDto dto) throws Exception;

    ThirdLoginVo thirdLogin(ThirdLoginDto dto) throws Exception;

    SysToken bindPhone(BindPhoneDto dto) throws Exception;

    OneKeyVo oneKey(OneKeyLoginTokenDto dto) throws Exception;

    UserVo findUserInfo() throws Exception;

    EditUserVo editUserInfo(EditUserDto dto) throws Exception;

}

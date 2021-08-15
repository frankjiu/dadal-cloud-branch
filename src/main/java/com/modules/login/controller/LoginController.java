package com.modules.login.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.core.anotation.AuthCheck;
import com.core.anotation.Logged;
import com.core.config.sms.JiGuangSms;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.RedisUtil;
import com.modules.login.model.dto.*;
import com.modules.login.model.vo.*;
import com.modules.login.service.AliLoginService;
import com.modules.login.service.LoginService;
import com.modules.login.service.WxLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @Description: 手机号登录, 一键登录, 第三方登录及手机号, 三方绑定
 * @Author: QiuQiang
 * @Date: 2021-05-22
 */
@RestController
@RequestMapping("/login")
@Validated
@Slf4j
@Api(tags = "01-登录")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private WxLoginService wxLoginService;

    @Autowired
    private AliLoginService aliLoginService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JiGuangSms jiGuangSms;

    /**
     * 发送短信
     *
     * @param dto
     * @return
     */
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "发送短信验证码")
    @ApiOperation(value = "发送短信验证码", notes = "app客户端调用,返回msgId")
    @PostMapping("/send")
    public HttpResult send(@RequestBody @Valid SendMsgDto dto) {
        String messageId = jiGuangSms.sendSmsCode(Constant.JiGuang.TEMPID_DYNAMIC_CODE, dto.getMobile());
        // 缓存 msgId 5分钟
        redisUtil.set(dto.getMobile(), messageId, 300L);
        return HttpResult.success();
    }

    /**
     * 校验短信
     *
     * @param dto
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiIgnore
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "校验短信验证码")
    @ApiOperation(value = "校验短信验证码(可测试验证码的校验, 无账户创建, 不作单独调用)")
    @PostMapping("/check")
    public HttpResult check(@RequestBody @Valid PhoneLoginCheckDto dto) {
        Object msgId = redisUtil.get(dto.getMobile());
        if (StringUtils.isEmpty(msgId)) {
            return HttpResult.fail(RespCode.CODE_EXPIRED);
        }
        boolean result = jiGuangSms.checkCode(msgId.toString(), dto.getCode());
        if (!result) {
            return HttpResult.fail(RespCode.CODE_ERROR);
        }
        return HttpResult.success();
    }

    /**
     * 手机登录(参数: phone, code: 返回: token, bindOther)
     * 使用手机号, 校验验证码登录
     * 查询用户的三方绑定信息: 是否已绑定第三方
     *
     * @param dto: phone, code
     * @return token, bindOther
     */
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "手机登录")
    @ApiOperation(value = "手机登录")
    @PostMapping("/phoneLogin")
    public HttpResult<PhoneLoginVo> phoneLogin(@RequestBody @Valid PhoneLoginAndCheckDto dto) throws Exception {
        //测试代码开始
        if (Constant.Test.TEST_MOBILE.equals(dto.getMobile()) && Constant.Test.TEST_CODE.equals(dto.getCode())) {
            PhoneLoginVo vo = loginService.phoneLogin(dto.getMobile());
            return HttpResult.success(vo);
        }
        //测试代码结束
        // 验证码校验
        Object msgId = redisUtil.get(dto.getMobile());
        if (StringUtils.isEmpty(msgId)) {
            return HttpResult.fail(RespCode.CODE_EXPIRED);
        }
        boolean result = jiGuangSms.checkCode(msgId.toString(), dto.getCode());
        if (!result) {
            return HttpResult.fail(RespCode.CODE_ERROR);
        }
        PhoneLoginVo vo = loginService.phoneLogin(dto.getMobile());
        return HttpResult.success(vo);
    }

    /**
     * 是否需要绑定三方
     *
     * @return show, wechat, alipay
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "是否需要绑定三方")
    @ApiOperation(value = "是否需要绑定三方", notes = "前提条件: 已通过手机注册")
    @PostMapping("/needOther")
    public HttpResult<NeedOtherVo> needOther() throws Exception {
        NeedOtherVo vo = loginService.needOther();
        return HttpResult.success(vo);
    }

    /**
     * 用户登录后, 绑定三方(type: 0微信 1支付宝, code):
     * 更新用户的跨店卡信息
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "绑定三方")
    @ApiOperation(value = "绑定三方", notes = "用户登录后执行的操作")
    @PostMapping("/bindOther")
    public HttpResult bindOther(@RequestBody @Valid BindOtherDto dto) throws Exception {
        return loginService.bindOther(dto);
    }

    /**
     * 第三方登录
     *
     * @param dto code, type: 0微信 1支付宝
     * @return token, bindPhone, openId
     */
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "第三方登录")
    @ApiOperation(value = "第三方登录", notes = "type三方标识: 0-微信,1-支付宝")
    @PostMapping("/thirdLogin")
    public HttpResult<ThirdLoginVo> thirdLogin(@RequestBody @Valid ThirdLoginDto dto) throws Exception {
        ThirdLoginVo thirdLoginVo = loginService.thirdLogin(dto);
        return HttpResult.success(thirdLoginVo);
    }

    /**
     * 一键登录--通过loginToken验证后返回解密手机号:
     * 认证成功, 查询是否已有账号:
     * 有账号: 无需创建
     * 无账号: 创建账号
     * 返回登录token 和 是否已绑定第三方
     *
     * @param dto
     * @return
     */
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "一键登录")
    @ApiOperation(value = "一键登录")
    @PostMapping("/oneKey")
    public HttpResult<OneKeyVo> oneKey(@RequestBody @Valid OneKeyLoginTokenDto dto) throws Exception {
        OneKeyVo vo = loginService.oneKey(dto);
        return HttpResult.success(vo);
    }

    /**
     * 绑定手机号(参数: 手机号, 验证码, openId, type:微信/支付宝)
     * 参数校验, 注册账户, 登录成功
     */
    @Logged(type = Constant.Log.TYPE_LOGIN, module = Constant.Log.MODULE_USER, remark = "绑定手机号")
    @ApiOperation(value = "绑定手机号")
    @PostMapping("/bindPhone")
    public HttpResult<SysToken> bindPhone(@RequestBody @Valid BindPhoneDto dto) throws Exception {
        // 验证码校验
        Object msgId = redisUtil.get(dto.getPhone());
        if (msgId == null) {
            return HttpResult.fail(RespCode.CODE_EXPIRED);
        }
        boolean valid = jiGuangSms.checkCode(msgId.toString(), dto.getCode());
        if (!valid) {
            return HttpResult.fail(RespCode.CODE_INVALID);
        }

        SysToken sysToken = loginService.bindPhone(dto);
        return HttpResult.success(sysToken);
    }

    /**
     * 获取用户信息
     *
     * @param
     * @return
     * @throws Exception
     */
    @AuthCheck
    @ApiOperation(value = "获取用户信息", notes = "登录后才能获取")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "获取用户信息")
    @PostMapping("/findUserInfo")
    public HttpResult<UserVo> findUserInfo() throws Exception {
        UserVo vo = loginService.findUserInfo();
        return HttpResult.success(vo);
    }

    /**
     * 修改用户信息
     *
     * @param
     * @return
     * @throws Exception
     */
    @AuthCheck
    @ApiOperation(value = "修改用户信息", notes = "登录后才能修改")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "修改用户信息")
    @PostMapping("/editUserInfo")
    public HttpResult<EditUserVo> editUserInfo(@RequestBody @Valid EditUserDto dto) throws Exception {
        EditUserVo vo = loginService.editUserInfo(dto);
        return HttpResult.success(vo);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_LOGOUT, module = Constant.Log.MODULE_USER, remark = "退出登录")
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public HttpResult logout() throws Exception {
        loginService.logout();
        return HttpResult.success();
    }

    /**
     * 生成微信授权参数: 通过code获取access_token
     *
     * @param code
     * @return
     * @throws Exception
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "生成微信授权参数")
    @ApiOperation(value = "生成微信授权参数", notes = "app客户端调用")
    @PostMapping("/getAccessToken")
    public HttpResult<AccessTokenVo> getAccessToken(String code) throws Exception {
        AccessTokenVo accessTokenVo = wxLoginService.getAccessToken(code);
        return HttpResult.success(accessTokenVo);
    }

    /**
     * 校验access_token是否有效
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "校验access_token是否有效")
    @ApiOperation(value = "校验access_token是否有效")
    @PostMapping("/isAccessTokenIsValid")
    public HttpResult isAccessTokenIsValid(@RequestBody @Valid CheckAccessTokenDto dto) throws Exception {
        boolean isValid = wxLoginService.isAccessTokenIsValid(dto.getAccessToken(), dto.getOpenId());
        return HttpResult.success(isValid);
    }

    /**
     * 生成客户端调用授权码接口的sign参数(app客户端调用,为了安全在服务端生成)
     *
     * @param userId 该次用户授权请求的 ID，该值在商户端应保持唯一
     * @return
     */
    // @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "生成阿里授权接口参数")
    @ApiOperation(value = "生成阿里授权接口参数", notes = "app客户端调用")
    @PostMapping("/getSign")
    public HttpResult<WxParamVo> getSign(String userId) throws Exception {
        return aliLoginService.getSign(userId);
    }

    /**
     * 获取支付宝用户令牌 (app客户端调用,auth_code -> auth_token)
     *
     * @param dto app客户端调用sdk接口生成的用户登录授权码
     * @return
     */
    // @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "用授权码获取支付宝用户令牌")
    @ApiOperation(value = "用授权码获取支付宝用户令牌", notes = "app客户端生成登录授权码后调用")
    @PostMapping("/getAuthToken")
    public HttpResult<AccessTokenVo> getAuthToken(@RequestBody @Valid AliAuthCodeDto dto) throws Exception {
        AccessTokenVo accessTokenVo = aliLoginService.getAuthToken(dto.getAuthCode());
        return HttpResult.success(accessTokenVo);
    }

    /**
     * 获取支付宝用户信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_USER, remark = "获取阿里用户信息")
    @ApiOperation(value = "获取阿里用户信息", notes = "app客户端获取到支付宝用户令牌后调用")
    @PostMapping("/getUserInfo")
    public HttpResult<AliUserInfoVo> getUserInfo(@RequestBody @Valid AliAuthTokenDto dto) throws Exception {
        AliUserInfoVo userInfo = aliLoginService.getUserInfo(dto.getAuthToken());
        return HttpResult.success(userInfo);
    }

}

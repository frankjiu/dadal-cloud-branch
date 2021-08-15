package com.core.result;

import lombok.Getter;

/**
 * @Description: 响应码
 * @author: Frankjiu
 * @date: 2020年6月3日
 */
@Getter
public enum RespCode {

    // 通用状态码
    SUCCESS(10000, "操作成功!"),
    UNAUTHORIZED(11001, "未登录!"),
    INSUFFICIENT_AUTHORITY(11002, "权限不足!"),
    INVALID_PARAM(12001, "参数非法!"),
    PARSE_ERROR(13001, "解析错误!"),
    METHOD_ERROR(14001, "请求方法错误!"),
    API_ERROR(15001, "远程API请求失败!"),
    API_TIME_OUT(15002, "远程API请求超时!"),
    DUPLICATED(16001, "重复数据!"),
    INTERNAL_ERROR(90000, "对不起, 系统繁忙, 请稍后重试!"),
    NOT_FOUND(91000, "资源不存在!"),
    FAIL(99999, "操作失败!"),

    // 业务状态码
    SEND_CODE_FAILED(21001, "验证码发送失败!"),
    CODE_EXPIRED(21003, "验证码已过期!"),
    CODE_ERROR(21004, "验证码错误!"),
    CODE_INVALID(21005, "验证码无效!"),
    JIGUANG_CHECK_FAILED(21007, "调用极光验证失败!"),
    JIGUANG_TOKEN_EXPIRED(21008, "极光token已过期!"),

    USER_NOT_FOUND(22001, "用户不存在!"),
    USER_ACCOUNT_NOT_FOUND(22002, "用户账号不存在!"),
    NO_TOKEN(22003, "请传递 token!"),
    AUTHENTICATION_FAILED(22004, "权限无效!"),
    WX_LOGIN_FAILED(22005, "微信认证失败!"),
    MOBILE_IN_USE(22006, "该手机号已被占用!"),
    GET_TOKEN_FAILED(22007, "获取用户令牌失败!"),
    LOGIN_EXPIRED(22008, "登录已过期!"),
    CODE_BEEN_USED(22009, "该code已被使用过!"),
    TOKEN_IS_RENEWED(22010, "请使用最新token!"),
    WX_ALLREADY_BOUND(22011, "已绑定过微信!"),
    ALI_ALLREADY_BOUND(22012, "已绑定过支付宝!"),

    COMMENT_EXPIRED(23001, "评论时效已过!"),
    RESTAURANT_NOT_EXIST(23002, "收款餐厅不存在!"),

    GET_USERINFO_FAILED(24001, "获取用户信息失败!"),

    STAFF_NOT_FOUND(25001, "该员工不存在!"),
    STAFF_PASSWORD_ERROR(25002, "用户名或密码错误!"),

    //优惠券
    REWARD_NOT_EXIST(26001, "没有查询到奖品信息！"),
    REWARD_DISABLED_ERROR(26002, "所选奖励已使用或已过期！"),
    REWARD_INFO_ERROR(26003, "所选奖励信息错误,请联系商家！"),
    REWARD_CARD_MATCH_ERROR(26004, "所选奖励没有匹配的会员卡！");


    private int code;
    private String description;

    RespCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

}

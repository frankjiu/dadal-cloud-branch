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
    SUCCESS(1000, "操作成功!"),
    INTERNAL_ERROR(1001, "对不起, 系统繁忙, 请稍后重试!"),
    INVALID_PARAM(1004, "参数非法!"),
    DUPLICATED(1005, "重复数据!"),
    NOT_FOUND(1006, "资源不存在!"),
    METHOD_ERROR(1007, "请求方法错误!"),
    STAFF_ID_NOT_FOUND(1008, "请求缺失员工信息!"),
    ACCOUNT_ID_NOT_FOUND(1009, "请求缺失管理信息!"),
    NO_TOKEN(1011, "请传递 token!"),
    AUTHENTICATION_FAILED(1012, "权限无效!"),
    TOKEN_IS_RENEWED(1013, "请使用最新token!"),
    TOKEN_ERROR(1014, "token不正确!"),
    INVALID_STAFF_ID(1015, "该员工不存在"),
    INVALID_ACCOUNT_ID(1016, "该管理员不存在"),
    ACCOUNT_NO_RIGHT_ACCESS(1017, "无权限访问非自身旗下餐厅"),
    PRICE_LIMIT(1019, "金额输入错误，请输入1-10000元"),
    PHONE_ERROR(1020, "请输入正确的手机号!"),
    UNAUTHORIZED(1100, "未登录!"),
    AS_USR_REPEAT_LOGIN(1101, "当前用户在其他地方登录，请重新登录!"),
    INSUFFICIENT_AUTHORITY(1103, "权限不足!"),
    AS_USR_PWD_ERROR(1104, "用户名/密码错误"),
    FAIL(9999, "操作失败!"),

    // 业务状态码
    AS_USERNAME_EXIST(1111, "用户名已存在"),
    AS_USERNAME_INVALID(1112, "用户名不合法,长度6-20位，由数字，字母，下划线组成，不能以数字开头"),
    AS_PASSWORD_INVALID(1113, "密码不合法!长度为8-20位，由数字，大小写字母，下划线组成，且至少包含两种及以上字符"),

    RS_REST_NOT_EXIST(1304, "该餐厅不存在"),
    RS_OWNER_NOT_EXIST(1306, "该后台账号不存在!"),

    SS_USERNAME_EXIST(1400, "用户名已存在!"),
    SS_STAFF_NOT_EXIST(1401, "员工账户不存在!"),

    FS_FOOD_NOT_EXIST(1500, "该菜品不存在!"),
    FS_NOT_OWNER(1502, "只能查看本人名下的餐厅餐品!"),
    FS_MEMBER_PRICE_HIGH(1512, "会员价不能高于菜品原价"),
    FS_FOOD_NAME_EXIST(1513, "菜品名已存在"),

    FTS_TYPE_NOT_EXIST(2201, "菜品分类不存在"),
    FTS_TYPE_USED(2202, "该分类下存在菜品"),

    CS_CHAIN_NOT_EXIST(2400, "该餐厅连锁不存在"),
    CS_CHAIN_NOT_EXIST_OR_NO_RIGHT(2401, "该餐厅连锁不存在/不属于当前用户旗下"),
    CS_RESTAURANT_USED(2402, "餐厅连锁旗下存在餐厅时无法删除"),
    CS_EXIST_BALANCE_CARD(2403, "该连锁旗下存在未消费的会员卡，无法删除"),

    MS_STAFF_NOT_EXIST(2502, "员工账户不存在");

    private int code;
    private String description;

    RespCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

}

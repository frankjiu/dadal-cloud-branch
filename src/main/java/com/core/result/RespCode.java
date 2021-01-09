package com.core.result;

import lombok.Getter;

/**
 * @Description: 响应码
 * @author: Frankjiu
 * @date: 2020年6月3日
 */
@Getter
public enum RespCode {

    /**
     * 成功
     */
    SUCCESS("11111", "Success"),

    /**
     * 失败
     */
    FAIL("00000", "Fail"),

    /**
     * 参数错误
     */
    PARAM_ERROR("90001", "Invalid parameters"),

    /**
     * 绑定错误
     */
    BIND_ERROR("90002", "Bind error"),

    /**
     * 认证失败
     */
    AUTHORIZATION_ERROR("90003", "Login timed out! please login again"),

    /**
     * TOKEN验证错误
     */
    TOKEN_ERROR("90004", "TOKEN verification error"),

    /**
     * 方法错误
     */
    METHOD_ERROR("90005", "Request method error"),

    /**
     * 解析错误
     */
    PARSE_ERROR("90006", "Parse failed"),

    /**
     * 权限不足
     */
    INSUFFICIENT_AUTHORITY("90007", "Permissions is required!"),

    /**
     * 资源不存在
     */
    NOT_FOUND("90008", "Resource not found"),

    /**
     * 系统错误
     */
    INTERNAL_ERROR("90009", "Internal System Error");

    private String code;
    private String description;

    RespCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}

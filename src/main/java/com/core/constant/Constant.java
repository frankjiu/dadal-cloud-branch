/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @author: Frankjiu
 * @date: 2020年8月27日
 * @version: V1.0
 */
package com.core.constant;

import java.time.ZoneId;

/**
 * @Description: 常量类
 * @author: Frankjiu
 * @date: 2020年8月27日
 */
public class Constant {

    public static final String APPLICATION_NAME = "DADAL";

    ////////////////////////////////////sys_constant/////////////////////////////////////////
    /** 登录用户 */
    public static final String SESSION_LOGIN_USER_ID = "_SESSION_LOGIN_USER_ID";

    public static final String SESSION_RANDOM_CODE = "_SESSION_RANDOM_CODE";

    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";

    /**
     * 系统时区设置
     */
    public final static ZoneId TIME_ZONE = ZoneId.of("Asia/Shanghai");

    /** demo */
    public class ConstDemoPartDvc {
        public static final String SESSION_DEMO_SIGN = "_SESSION_DEMO_SIGN";
        public static final String SESSION_DEMO_PREFIX = "_SESSION_DEMO_PREFIX";
    }

    ////////////////////////////////////redis_constant/////////////////////////////////////////
    /**
     * redis中TOKEN前缀
     */
    public static final String REDIS_TOKEN_PREFIX = APPLICATION_NAME + "::TOKEN::";

    /**
     * redis中用户信息的前缀
     */
    public static final String REDIS_USER_PREFIX = APPLICATION_NAME + "::USER::";

    /**
     * 登录之后过期的时间,默认是3天
     */
    public static final long LOGIN_EXPIRE = 60 * 60 * 1;

}
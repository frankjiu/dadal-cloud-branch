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

    public static final String APPLICATION_NAME = "HYZC_BS";

    /**
     * 系统时区设置
     */
    public final static ZoneId TIME_ZONE = ZoneId.of("Asia/Shanghai");

    //员工默认密码
    public static final String STAFF_DEFAULT_PWD = "123456";

    //当前登录员工信息
    public static final String REQUEST_VERIFY_INFO = "request_verify_info";

    /**
     * redis中TOKEN前缀
     */
    public static final String TOKEN_PREFIX = APPLICATION_NAME + "::TOKEN::";

    /**
     * redis中用户信息的前缀
     */
    public static final String USER_PREFIX = APPLICATION_NAME + "::USER::";

    public static final long LOGIN_EXPIRE_TIME = 60L * 60 * 3;

    public static final String JSON_FILE_PATH = "/home/ai/file/";

    public static final String JSON_FILE_SUFFIX = ".txt";

    /**
     * 七牛云对象实例参数
     */
    public class QiniuDisk {
        // ACCESS_KEY
        public static final String ACCESS_KEY = "d89mVd3lEer3adfFq0QIIhy0fh6K0s7t9U4GWnt6";
        // SECRET_KEY
        public static final String SECRET_KEY = "MNEnu2YglIDtyeccksnNK3RpPtT7xIxct2eTvb4E";
        // 华北区
        public static final String NORTH_AREA = "z1";
        // 空间
        public static final String AREA_NAME = "ddl-cloud";
        // 域名
        public static final String DOMAIN_NAME = "ddl.com";
    }

    //各类价格上限限制
    public static final Long PRICE_LIMIT = 1000000L;

}
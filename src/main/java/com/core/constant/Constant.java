/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @author: Frankjiu
 * @date: 2020年8月27日
 * @version: V1.0
 */
package com.core.constant;

import java.time.ZoneId;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @Description: 常量类
 * @author: Frankjiu
 * @date: 2020年8月27日
 */
public class Constant {

    public static final String APPLICATION_NAME = "DDL-CLOUD";

    /**
     * 系统时区设置
     */
    public final static ZoneId TIME_ZONE = ZoneId.of("Asia/Shanghai");

    /**
     * redis中TOKEN前缀
     */
    public static final String TOKEN_PREFIX = APPLICATION_NAME + "::TOKEN::";

    /**
     * redis中用户信息的前缀
     */
    public static final String USER_PREFIX = APPLICATION_NAME + "::USER::";

    public static final String STAFF_PREFIX = APPLICATION_NAME + "::STAFF::";

    public static final String INCR_ID = APPLICATION_NAME + "::INCR_ID::";

    public static final long INCR_ID_EXPIRE_TIME = 60L;

    public static final long LOGIN_EXPIRE_TIME = 90 * 24 * 60 * 60L; //一小时

    public static final String ORDER_WXPAY_LOCK_ = "ORDER_WXPAY_LOCK_";//微信订单回调锁
    //会员卡前缀
    public static final String CARD_QRCODE_PREFIX = "WL_";
    //跨店卡前缀
    public static final String CROSS_QRCODE_PREFIX = "KD_";
    //二维码密码
    public static final String QRCODE_PASSWORD = "test123pass";

    //会员卡锁
    public static final String MEMBER_CARD_LOCK_ = "MEMBER_CARD_LOCK_";

    public static final String MEMBER_CARD_PHONE_LOCK_ = "MEMBER_CARD_PHONE_LOCK_";

    public static final long ORDER_NOTIFY_LOCK_TIME = 300000L;//微信订单回调锁过期时间

    /**
     * 极光认证与短信发送常量
     */
    public class JiGuang {
        public static final String APP_KEY = "66685cc3db32854a9b92e1c0";
        public static final String MASTER_SECRET = "777f2316cb6241b9b569c0dc";
        // 动态码登录 短信模板
        public static final int TEMPID_DYNAMIC_CODE = 1; //195572;
        // 私钥
        public static final String PRIVATE_KEY = "555CdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKzz/ZoVH0xyTeUdm/wrBkPw2UVPWBHx5qK96iWI67n0KRg4QEk64zOB3CfugoQ4" +
                "+ckhDHiIPETdhHy6pYyh2Kg/Crpb5Hu8KEFmstIMNsBz0d6jWURyICNZ99pZrKtTPbRv0ZFhdy2UGMoVNZ7H5bfNdZZ3wmNcgqT5FiNSFtPRAgMBAAECgYAG4e7FovGL6UCQTY1Xi57ENWFwEKhAjo/PCh+3PquiysSX4iUsxNslwZUxQZyQBnRqPJZydXlwhCAxlnvgDNDCUHqmLJou/qmUfstMiFcVpNLoiEzazhxjWiiXuVR1a3QkQjYeO49OofuiN62WxRVofNVXDB8v744GCG92qlspwQJBAN3YmvxcOClXaMzzSaTlxac1/c0nqsFbMfFa96bx89VyTF24oGLH9RMd2D1/o5X+YKhZ5ZgVBLsvONFG3CJhfEsCQQDHlGoMQgV0IjRw7xutotimE3/uyzWrWgB4t2ZvkgFQ4Wwooxs8LW9YtW2XVlR0PfWM+lQRUqoj4ctTPuAi9ubTAkBsIPJRxIUfKVqMYpxnghLXkEpXX1ksC04JbKQ+u7l0NNSh7aZG311FZwuJ1heeuTDVfZOZj80Ri0+vOVF8dHWHAkEAkBqBV8hz15667rA28/CZLZD21pyza73YiVEjDXwGpzecuNrMV28W7p5ROuyCnkv+eIRnHvrpDm+CN+E2RQixMQJAYwspoOOBffa6MecKpp82uOtaR7boWD0nibmCdr3S5FfldRkeh7iwsCDN0BRo2lc8eVfU9+eYSFRvNC94INMsQw==";
        // 极光认证URL: 提交loginToken, 验证后返回手机号码
        public static final String VERIFY_URL = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
        // 短信发送与验证URL
        public static final String SEND_OR_CHECK_MSG_VALID_URL = "https://api.sms.jpush.cn/v1/codes"; // 自定义发送与校验需要成对进行
    }

    //测试账号
    public class Test {
        public static final String TEST_MOBILE = "18660868066";

        public static final String TEST_CODE = "095270";
    }

    /**
     * 支付宝相关参数
     */
    public class ALiPay {
        public static final String URL = "https://openapi.alipay.com/gateway.do";

        public static final String APP_ID = "2021002145607382";

        public static final String PID = "2088931304629650";

        public static final String SIGN_TYPE = "RSA2";

        public static final String CHARSET = "UTF-8";

        public static final String FORMAT = "json";

        public static final String QRCODE_TIMEOUT = "5m";

        public static final String NOTIFY_URL = "http://dadal.cloud.com:8083/notify/aliPayNotify";

        public static final String APP_NOTIFY_URL = "http://dadal.cloud.com:8083/notify/aliRechargeNotify";

        public static final String MEMBER_NOTIFY_URL = "";
        //应用私钥
        public static final String APP_PRIVATE_KEY = "111EvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCVnEdBXvORPfs6fXdE7xzANACspU1Qd0KthwGz0QJvEj0S" +
                "/LehLbXTbS48MVl46U2iUeuAZwC5ULMcANGsi8raatSPvzY2upca7I/QhoDn23AKDTCsfvm7L7SIynkU4QSiCdr8HSZNyktsQgCq9JW3pqvGbpXOxkwT5swMNadecR9hJ3DTRozsxBBVz34hkbi4cSWcC3PXCxpbVJ/ReKZUkfXtU6MhrJcoQSvcHj8nbCHiNMl55MM5l6vzDzYrWV+Sy3nhDDBjOEZzlq6F4MZWDqqml5av8km22bP8EakR5w/cGuegsKixq8AtTU+N4iq6uN0s0WaS0PLCQmoCb+klAgMBAAECggEAfrDVIkVOynqLKQ+gFS7K0PP4cjqUywQC4cFGrsfaYMY4E8EPmxyDMaPuGCjOFHa0ODVY0OXolgUBd0A2TgAe6nC2yXdpaFT/qlwSuMl9lVmcedaLljb8MQgHwCJ9ADJAYxsnZoEks5EP53/I4vu1YMxx/9obX/tJ5zx1JA23SmADY8g0sftnyt0qXgD54kVb8RhLT4nD+rU601gF0AFC7uO7gNGoYU48n/CIsi6vwHtZ0tDyJK3rO9xj4YeGR2uYG/4TrNpxaux9g0dtkEDnm81yhpgIGkX2yDpYlzhJJKPH+PjtdpM0XrO+mFdHujRFjIViHdBpQs8B/MIW8hI5YQKBgQDIA35Aq7f0K40XOcfie16yA3bI1WPiS/IRbrSKbNEJ2lCSpJGWCQ8p/V2wwVGvd1pnkiq/YkMb2DPMWgHR2oHa3JxNCII2RDXOgrWI6qeZVNV7bjhDUeLfbMIj4WfqWph2FCcDMOK5HFAygtIZ/b68VQuJgmm1FHLuD94+4UQjqQKBgQC/fQMDHRlk5nt0XnZrL3ME+sm2mmAXACAI466DCnDOXW5ibaRHYTysBrRfFAe9b/a9uuIlx8qwtDv4ABHJ7sqdISMh7XLS5qCGtl7GORna6AqgFTlyPDvZw1FFSRjBpnFYuO8GFKRYuF+TDam6yO1m5JwHvwpiTud/FO5OfbRHHQKBgQChzoGim8iTVXAsq+fShu+Sn07HJipJ9hp8k8ymd2WczOqF13CXqfiEhZ330kzc4HdYdlzyuvGDYyJ0XRQ/yr9maTZg81Mzx3an/AIUI5E2b/TMh9woSeR5zyg3krBYXLYeQCtXX4LrMcaHNmH1+NsGzN4qfkA557Mad+fZgq93EQKBgCYw+bvSPvFX4sGH4ZhpI0iX+Ek6KvYLaVg8iN7L40nVaqGo+n3eD+rDf5AC/7Qxs4WN03LzkSKDH/7LjMC4pqKGLGqk4CTeKKAVmXtxgg2eABiDgyychNde1EO8W3qsBqlKYSnWThz/0NgUfoipcGf4KoKFAL4wvI0lEI6JFb6lAoGAEg3+SsfWNg7M6cF6iymCdV/dqkEaIIHba8KMsBfpfiqT/ccsUcBSBtuwzMFlWQWJ9Gdo/nQ0p2MApqmLf537MpuLc5a5N0mzGrzV0kUghz5YtJoaNy/ANMi9VYU9XVLuRtqfGBA3KHICKqQ9QaPFv4pvOb/dbvylbylWlrlrL90=";
        //应用公钥
        public static final String APP_PUBLIC_KEY =
                "222BIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlZxHQV7zkT37On13RO8cwDQArKVNUHdCrYcBs9ECbxI9Evy3oS21020uPDFZeOlNolHrgGcAuVCzHADRrIvK2mrUj782NrqXGuyP0IaA59twCg0wrH75uy+0iMp5FOEEogna/B0mTcpLbEIAqvSVt6arxm6VzsZME+bMDDWnXnEfYSdw00aM7MQQVc9+IZG4uHElnAtz1wsaW1Sf0XimVJH17VOjIayXKEEr3B4/J2wh4jTJeeTDOZer8w82K1lfkst54QwwYzhGc5auheDGVg6qppeWr/JJttmz/BGpEecP3BrnoLCosavALU1PjeIqurjdLNFmktDywkJqAm/pJQIDAQAB";
        //支付宝公钥
        public static final String ALIPAY_PUBLIC_KEY =
                "333BIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnr3gP9adVH97dXOcFThh1uKnR1SCtuy58LFaEJsrWbAYirVHjaURP1Ozmnar2NMUsxxMKQhqAy6R8hIPv5" +
                        "/ZRW7Up9XnfR3RpMi2lrwu7jTIOQ7uUCXdUe3XCRRk2daHNDHxZITRM7XH9R9EsEbgB7KaePC5i+pKVJwqwGGxldDBJS0pv1NoPYZfs8e/SrfdlbWn+ANzHlWqAw8qm1zufP83857BjoxiSbCdh4Mdmub7q2wONojrx6VcBG8uZQrnUX+mC709WcOl7d7LZoz5QVrBl974lkkMmfNTW3EhK0H7RzP5SLQrQfmp+6xQ/L9lzgpHQYIF23dfeMKBgFVyCQIDAQAB";

    }

    /**
     * 微信相关参数
     */
    public class WxPay {
        public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
        public static final String CHECK_TOKEN_URL = "https://api.weixin.qq.com/sns/auth";
        public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
        public static final String APPID = "wx71b316438f141617";
        public static final String SECRET = "f61b7bd25d898bdd6473d77ac81dbaee";
        public static final String AUTHORIZATION_CODE_BASE = "authorization_code"; //"authorization_code";
        public static final String AUTHORIZATION_CODE_USERINFO = "snsapi_userinfo";
        public static final String EXPIRES_TIME = "7200";
        public static final String NOTIFY_URL = "http://dadal.cloud.com:8083/notify/wxPayNotify";
        public static final String APP_NOTIFY_URL = "http://dadal.cloud.com:8083/notify/wxRechargeNotify";
        public static final String MEMBER_NOTIFY_URL = "";
        public static final String MCH_ID = "1610965286"; //商户ID
        public static final String KEY = "X7rNBi0IkKg9ROTKBGsbvN7VlXF1jBbJ";//签名的key
        //微信回调时使用的类型
        public static final String WX_PAY_TYPE_RECHARGE = "Recharge";
        public static final String WX_PAY_TYPE_MEMBER_RECHARGE = "MemberRecharge";
        public static final String WX_PAY_TYPE_CONSUMPTION = "Consumption"; //消费
    }

    /**
     * 支付方式相关参数
     */
    public class PaymentMode {
        //微信支付
        public static final int WX = 0;
        //支付宝支付
        public static final int ALI = 1;
    }

    // 第三方标识-微信
    public static final int WX = 0;
    // 第三方标识-支付宝
    public static final int ALI = 1;

    // APP用户默认数据
    public class UserDefaultInfo {
        public static final String COUNTRY = "CN";
        public static final String PROVINCE = "Shandong";
        public static final String CITY = "Jinan";
        public static final String BIRTHDAY = "1980-01-01";
        public static final int SEX = 1;
        // 头像链接
        public static final String HEAD_IMG = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0119d95c482e8fa801213f26847b0f.jpg%402o" +
                ".jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626587599&t=372278c4e9723ac80efc26e97775afb5";
    }

    // 广告图存放位置
    public static final String AD_IMG_PATH = "/home/file/upload/images/";

    // 图片后缀
    public static final List SUF_LIST = asList("jpg", "png", "jpeg", "bmp");

    /**
     * 七牛云对象实例参数
     */
    public class QiniuDisk {
        // ACCESS_KEY
        public static final String ACCESS_KEY = "111mVd3lEernYDdFq0QIIhy0fh6K0s7t9U4GWnt6";
        // SECRET_KEY
        public static final String SECRET_KEY = "222nu2YglIDxAzccksnNK3RpPtT7xIxct2eTvb4E";
        // 访问域名
        public static final String DOMAIN_URL = "http://dadal.cloud.com/";
        // 对象空间名称
        public static final String AREA_NAME = "huayingzc";

        public static final String COMMENT_AREA_NAME = "huayingzc-comment";

        public static final String COMMENT_DOMAIN_NAME = "cmt.dadal.cloud.com";
        public static final String PRE_FIX = "http://";
    }

    /**
     * 日志常量
     */
    public class Log {
        /**
         * 操作类型
         */
        public static final String TYPE_LOGIN = "登录";
        public static final String TYPE_LOGOUT = "退出";
        public static final String TYPE_ADD = "新增";
        public static final String TYPE_DEL = "删除";
        public static final String TYPE_MODIFY = "修改";
        public static final String TYPE_QUERY = "查询";

        /**
         * 业务模块
         */
        public static final String MODULE_SYS = "系统级";
        public static final String MODULE_USER = "用户模块";
        public static final String MODULE_INDEX = "APP首页";
        public static final String MODULE_ORDER = "订单模块";
        public static final String MODULE_COMMENT = "点评模块";
        public static final String MODULE_DINERECORD = "就餐记录模块";
        public static final String MODULE_CARD = "会员卡模块";
        public static final String MODULE_PAY = "支付模块";
        public static final String MODULE_REWARD = "奖励模块";
        public static final String MODULE_AD = "广告模块";
        public static final String MODULE_DISTINGUISH = "百度识别";
    }

    // 充值方式
    public class ChargeType {
        public static final String PAYMENT_MODE_ALI = "0";
        public static final String PAYMENT_MODE_WX = "1";
    }

    public class RewardType {
        public static final String FULL_CUT_REWARD = "满减券";
        public static final String CASH_REWARD = "现金券";
        public static final String REAL_ITEM_REWARD = "实物券";
        public static final String ALL_CUT_REWARD = "霸王餐";
    }

    //奖品提醒顶部图片
    public static final String PRIZE_HEAD_IMAGE = "http://common.dadal.cloud.com/%E5%A5%96%E5%93%81%E6%8F%90%E7%A4%BA%E5%A4%B4%E5%9B%BE%E7%89%87.png";
    //员工信息key
    public static final String REQUEST_VERIFY_INFO = "request_verify_info";
    //奖品提示有效期
    public static final Long PRIZE_ALERT_TIME = 3 * 30 * 24 * 60 * 60 * 1000L;
    //默认头像
    public static final String DEFAULT_AVATAR = "http://common.dadal.cloud.com/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F" +
            ".png";

    public static final String[] COMMON_VIDEO_TYPE = {"mpeg", "mpg", "dat", "avi", "navi", "asf", "wmv", "mov", "3gp",
            "mp4", "mkv", "flv", "rmvb", "qsv", "webm"};


    public static final String SESSION_RANDOM_CODE = "SESSION_RANDOM_CODE";

    //微信回调时使用的类型
    public static final String WX_PAY_TYPE_RECHARGE = "Recharge";
    public static final String WX_PAY_TYPE_MEMBER_RECHARGE = "MemberRecharge";
    public static final String WX_PAY_TYPE_CONSUMPTION = "Consumption";

    //各类价格上限限制
    public static final Long PRICE_LIMIT = 1000000L;

    //图片后缀
    public static final String[] USUALLY_PICTURE_SUFFIX = {".jpg", ".png", ".jpeg", ".bmp"};

    // 视频后缀允许格式
    public static final String[] USUALLY_VIDEO_SUFFIX = { ".rar", ".doc", ".docx", ".zip",
            ".pdf", ".txt", ".swf", ".xlsx", ".gif", ".png", ".jpg", ".jpeg",
            ".bmp", ".xls", ".mp4", ".flv", ".ppt", ".avi", ".mpg", ".wmv",
            ".3gp", ".mov", ".asf", ".asx", ".vob", ".wmv9", ".rm", ".rmvb" };

}
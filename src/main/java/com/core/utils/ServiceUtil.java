package com.core.utils;

import com.core.constant.Constant;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @Description:
 * @date: 2021/6/1/0001 16:13
 * @author: YuZHenBo
 */

public class ServiceUtil {

    private static final String REQUEST_VERIFY_INFO = "request_verify_info";
    //校验手机是否合规 国内手机号格式
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";


    /**
     * 从请求中获取员工信息
     * @param request
     * @return
     */
    public static Long getUserInfo(HttpServletRequest request) {
        //得到当前登录的staffID
        Object attribute = request.getAttribute(Constant.REQUEST_VERIFY_INFO);
        if (attribute == null) {
            return null;
        }
        return Long.parseLong(String.valueOf(attribute));
    }

    /**
     * 校验手机号
     *
     * @param phone 手机号
     * @return boolean true:是  false:否
     */
    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }
}

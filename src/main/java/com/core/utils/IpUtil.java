/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @Package: com.core.utils
 * @author: Frankjiu
 * @date: 2020年8月27日
 * @version: V1.0
 */

package com.core.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: Get the real IP of the request
 * @author: Frankjiu
 * @date: 2020年8月27日
 */
public class IpUtil {

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknow".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1")) {
                // 通过网卡获取本机ip配置
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inetAddress.getHostAddress();
            }
        }

        // 使用多个代理时,第一个IP为客户端真实IP,通过','分割处理获取第一个
        if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }

}

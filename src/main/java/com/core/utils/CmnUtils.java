package com.core.utils;

import com.core.constant.Constant;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmnUtils {

    /**
     * 得到带vip前缀的会员卡号
     * @param id
     * @return
     */
    public static String addMemberCardPrefix(Long id) {
        StringBuilder sb = new StringBuilder();
        sb.append("vip");
        sb.append(id);

        return sb.toString();
    }


    /**
     * 从带前缀的会员卡号中解析出会员卡号
     * @param physicalCardID
     * @return
     */
    public static Long parseMemberCardID(String physicalCardID) {
        String substring = physicalCardID.substring(3);
        return Long.parseLong(substring);
    }

    /**
     * 判断是否是手机号
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
        if(!StringUtils.isEmpty(str)){
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 获取指定某一天的开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static Long getDailyStartTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定某一天的结束时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static Long getDailyEndTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定某一天的时间为17:30的时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static Long getDailySomeTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);
        return calendar.getTimeInMillis();
    }

    /**
     * 凭借字符串
     * @param strings
     * @return
     */
    public static String concat(String... strings) {
        StringBuilder sb = new StringBuilder();
        for(String str : strings) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 得到七牛云文件名
     * @param file
     * @return
     */
    public static String getQnuFileName(MultipartFile file) {
        StringBuilder key = new StringBuilder();
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        key.append(UUID.randomUUID().toString().replaceAll("-", ""));
        key.append(suffix);
        return key.toString();
    }

    /**
     * 检查图片后缀
     * @param uploadImg
     * @return
     */
    public static boolean checkPicture(MultipartFile uploadImg) {
        String filename = uploadImg.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        List<String> suffixCollection = Arrays.asList(Constant.USUALLY_PICTURE_SUFFIX);
        return suffixCollection.contains(suffix);
    }

    /**
     * 检查视频后缀
     * @param uploadImg
     * @return
     */
    public static boolean checkVideo(MultipartFile uploadImg) {
        String filename = uploadImg.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        List<String> suffixCollection = Arrays.asList(Constant.USUALLY_VIDEO_SUFFIX);
        return suffixCollection.contains(suffix);
    }

    /**
     * Long类型金额转化为String
     * @param cost
     * @return
     */
    public static String computeCost(Long cost) {
        BigDecimal b1 = new BigDecimal(cost);
        BigDecimal b2 = new BigDecimal(100);
        BigDecimal divide = b1.divide(b2);
        return divide.toString();
    }

    public static String generateUUID() {
        return IdUtil.simpleUUID();
    }

    /**
     * 得到访问IP地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }
}

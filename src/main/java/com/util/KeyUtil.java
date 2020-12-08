package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * @Description: 定制数字型唯一主键(时间 + 随机数)
 * @Author: QiuQiang
 * @Date: 2020-12-08
 */
public class KeyUtil {
    public static synchronized String generateUniqueKey() {
        Random random = new Random();
        Integer randomInt = random.nextInt(900000000) + 100000000; // 9位随机数
        //Long time_13 = System.currentTimeMillis(); // 13位时间
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 17位时间
        String time17Str = formatter.format(new Date());
        return randomInt + time17Str;
    }
}
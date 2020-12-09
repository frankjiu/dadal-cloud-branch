package com.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Description: Base64加解密工具(UTF_8编码)
 * @Author: QiuQiang
 * @Date: 2020-12-08
 */
public class Base64Util {

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解密
     *
     * @return
     */
    public static String decode(String encodedStr) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);
        String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedStr;
    }


}

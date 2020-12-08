package com.modules.Encode;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-08
 */
public class Base64Test {

    @Test
    public void encodeTest() {
        String text = "http://www.baidu.com";

        // 加密
        String encodedStr = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodedStr);

        // 解密
        byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);
        String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println(decodedStr);

    }
}

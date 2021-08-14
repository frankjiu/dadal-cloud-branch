package com.core.utils;

import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Random;


/**
 * @Description: 生成指定位数的随机字符串
 * @date: 2021/5/17/0017 13:14
 * @author: YuZHenBo
 */
@Component
public class RandomStrUtil {

    public static String generateStr(int bound) {
        assert bound > 0 : "传入的字符串长度数值需要大于0";
        String baseStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bound; i++) {
            int index = new Random().nextInt(36);
            char s = baseStr.charAt(index);
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    public static String generateStrPlus(int bound) {
        assert bound > 0 : "传入的字符串长度数值需要大于0";
        String baseStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bound; i++) {
            int index = new Random().nextInt(62);
            char s = baseStr.charAt(index);
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    @Test
    public void test() {
        System.out.println(generateStrPlus(32));
    }

}

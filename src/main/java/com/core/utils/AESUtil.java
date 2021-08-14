package com.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @Description: 字符串AES加密解密
 * @Author: QiuQiang
 * @Date: 2021-05-31
 */
@Slf4j
public class AESUtil {

    /**
     * AES加密
     *
     * @param content  待加密字符串
     * @param password 加密密码
     * @return 密文
     */
    public static String encrypt(String content, String password) {
        try {
            // 创建KEY生产者
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //替换开始
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            //替换结束
            // 根据用户密码初始化KEY生产者, 并生成秘钥
//            kgen.init(128, new SecureRandom(password.getBytes("utf-8")));
            SecretKey secretKey = kgen.generateKey();
            // 获取专用秘钥
            SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            byte[] contentBytes = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 执行加密
            contentBytes = cipher.doFinal(contentBytes);
            return parseByte2HexStr(contentBytes);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param content  已经加密的字符串
     * @param password 加密密码
     * @return 明文
     */
    public static String decrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
//            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] contentByte = parseHexStr2Byte(content);
            contentByte = cipher.doFinal(contentByte);
            String decodeStr = new String(contentByte, "utf-8");
            return decodeStr;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 二进制转十六进制
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer stb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            stb.append(hex.toUpperCase());
        }
        return stb.toString();
    }

    /**
     * 十六进制转二进制
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String content = "a125frank";
        String password = "abc";
        // 加密
        String encodeStr = encrypt(content, password);
        System.out.println("密文:" + encodeStr);
        // 解密
        String decodeStr = decrypt(encodeStr, password);
        System.out.println("解密:" + decodeStr);
    }

}

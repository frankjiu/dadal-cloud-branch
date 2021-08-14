package com.core.utils;

/**
 * @Description: 号码解析
 * @Author: QiuQiang
 * @Date: 2021-05-24
 */

import com.wlhlwl.hyzc.customer.core.constant.Constant;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * RSA解密
 */
public class RSADecryptUtil {

    public static String decrypt(String content, String prikey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] b = Base64.getDecoder().decode(content);
        return new String(cipher.doFinal(b));
    }

    @Test
    public void testDecry() throws Exception {
        String phoneContent = "XScZuJbzZJ6e48OTXSaY3hmlhAEYwB1kKIFOrPHDBRoSo03X5a2iZ1i9JKI/SD/a6itM3" +
                "/VoYRBDa423wkMyZYyLkaAR9aHggJiPW85yjTNJmQhzlOawzIry7xGOnZfU5VpLdwy1fRjE80F7jhovHnTMFpW0WaWNdnKrT0nryxs=";
        String result = decrypt(phoneContent, Constant.JiGuang.PRIVATE_KEY);
        System.out.println(result);
    }

}
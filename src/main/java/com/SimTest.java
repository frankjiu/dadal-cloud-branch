package com;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Test
    public void test1() {
        String s = (new Sha256Hash("123456", "123456")).toHex();

        String property = System.getProperty("user.dir");
        System.out.println(property);
    }

}

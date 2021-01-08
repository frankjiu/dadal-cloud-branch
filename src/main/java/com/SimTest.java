package com;

import org.apache.commons.lang3.RandomStringUtils;
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

        String salt = RandomStringUtils.randomAlphanumeric(20);
        System.out.println(salt);
        System.out.println(new Sha256Hash("123456", salt).toHex());

    }

}

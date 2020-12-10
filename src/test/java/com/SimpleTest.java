package com;

import org.junit.Test;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-10
 */
public class SimpleTest {

    @Test
    public void simpleTest() {
        Integer num = 0;
        num = null;
        if (num <= 0) {
            System.out.println("failed");
        }
    }



}

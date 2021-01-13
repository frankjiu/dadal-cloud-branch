package com;

import org.junit.Test;

import java.io.File;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Test
    public void test1() {
        String uploadFilePath = "/usr/abb/temp";
        File file = new File(new File(uploadFilePath).getAbsolutePath());
        if (!file.exists()) {
            boolean f = file.mkdirs();
            System.out.println(f);
        }

    }

}

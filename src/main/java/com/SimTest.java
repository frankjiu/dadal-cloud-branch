package com;

import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Test
    public void test1() throws IOException, InterruptedException {

        String fileName = "tb_im.xlsx";

        String perName = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String destFileName = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + "_" + perName + "_" + "955688" + suffix;

        for (int i = 0; i < 6; i++) {
            String destFileName1 = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + "_" + perName + "_" + "955688" + suffix;
            Thread.sleep(1000);
            System.out.println(destFileName1);
        }

    }

}

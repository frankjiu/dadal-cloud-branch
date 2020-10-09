package com;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-23
 */
public class DemoTest {

    @Test
    public void testme(){
        Object thirdVal = "next";
        Object fourthVal = "30";
        boolean flag = "NEXT".equalsIgnoreCase(String.valueOf(thirdVal));

        LocalDate localDate = LocalDate.now().plusDays(Long.valueOf(fourthVal.toString()));

        System.out.println(localDate);
    }

}

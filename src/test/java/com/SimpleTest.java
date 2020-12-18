package com;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-10
 */
public class SimpleTest {

    @Test
    public void simpleTest() throws Exception {

        //List<String> list = List.of("9", "5", "8", "6", "", "12", "7", "", "25", "4");
        /*List<String> list = new ArrayList<>();
        list.add("9");
        list.add("5");
        list.add("");
        list.add("");
        list.add("8");
        list.add("8");
        list.add("");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("6");
        list.add("");

        Collections.sort(list, (s1, s2) -> {
            //return s1.compareTo(s2) >= 0 ? -1 : 1;
            //[9, 8, 8, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, , , , ]
            return s1.compareTo(s2) >= 0 ? 1 : -1;
            //[, , , , 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8, 8, 9]
        });

        System.out.println(list);*/

        Boolean flag = true;
        /*if (flag && StringUtils.isEmpty("aa")) {
            flag = true;
        } else if (flag && "aa".equals("aa1")) {
            flag = true;
        } else {
            flag = false;
        }*/

        if (!StringUtils.isEmpty("aa") && "aa".equals("aa")) {
            flag = flag && true;
        }

        System.out.println(flag);


    }


}

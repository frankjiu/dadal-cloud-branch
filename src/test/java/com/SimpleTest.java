package com;

import com.modules.base.model.entity.Demo;
import com.modules.base.model.entity.Demp;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-10
 */
public class SimpleTest {

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

        //Boolean flag = true;
        /*if (flag && StringUtils.isEmpty("aa")) {
            flag = true;
        } else if (flag && "aa".equals("aa1")) {
            flag = true;
        } else {
            flag = false;
        }*/

        /*if (!StringUtils.isEmpty("aa") && "aa".equals("aa")) {
            flag = flag && true;
        }

        System.out.println(flag);*/


        int a = 100;
        Integer b = 100;
        System.out.println(a == b);

        Integer c = 200;
        Integer d = 200;
        System.out.println(d == c);
        System.out.println(d.equals(c));

        Integer m = 200;
        Integer n = 200;

        Object m1 = m;
        Object m2 = n;
        System.out.println(m1 == m2);
        System.out.println(m1.equals(m2));


    }

    @Test
    public void test2(){
        Demp demp1 = new Demp();
        demp1.setDesc(new Demp.Desc("BBB"));
        Demp demp2 = new Demp();
        demp2.setDesc(new Demp.Desc("FFF"));
        Demp demp3 = new Demp();
        demp3.setDesc(new Demp.Desc("AAA"));
        Demp demp4 = new Demp();
        demp4.setDesc(new Demp.Desc("EEE"));
        Demp demp5 = new Demp();
        demp5.setDesc(new Demp.Desc("CCC"));
        //List<Demp> list = List.of(demp1,demp2,demp3,demp4,demp5); //这种无法排序.
        List<Demp> list = new ArrayList<>();
        list.add(demp1);
        list.add(demp2);
        list.add(demp3);
        list.add(demp4);
        list.add(demp5);

        list.stream().forEach(e -> System.out.println("排序前:" + e.getDesc().getName()));

        list.sort(Comparator.comparing(t -> t.getDesc().getName()));
        Collections.reverse(list);
        System.out.println("===================");

        list.stream().forEach(e -> System.out.println("排序后:" + e.getDesc().getName()));
    }

    @Test
    public void test3(){
        String property = System.getProperty("user.dir");
        System.out.println(property);

        Integer a = 1;
        String b = "1";
        Boolean f = b.equals(a.toString());
        System.out.println(f);

    }


}

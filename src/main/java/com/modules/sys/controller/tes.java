package com.modules.sys.controller;

import com.google.common.collect.Lists;
import com.modules.sys.model.entity.Demo;
import org.thymeleaf.util.DateUtils;

import javax.sql.rowset.serial.SQLOutputImpl;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-24
 */
public class tes {

    public static final List<String> thirdFieldList = new ArrayList<>() {{
        add("city");
        add("region");
        add("sub_region");
        add("country");
    }};

    public static void main(String[] args) {
        /*Object fourthVal = "50%";
        fourthVal = percentToDouble(fourthVal.toString());
        System.out.println(fourthVal.toString());*/

        /*boolean flag = false;
        Object thirdVal = "";

        String thirdValStr = thirdVal.toString();
        String[] thirdValArray = thirdValStr.split(",");
        if (thirdValArray.length > 1){
            flag = true;
        }
        System.out.println(flag);*/

        /*Object obj = "ss";
        boolean b = "ss".equalsIgnoreCase(obj.toString());
        System.out.println(b);*/

       /* boolean city = thirdFieldList.contains("city".toUpperCase());
        System.out.println(city);*/


        /*String str = "route_origin";
        String str2 = "route_destination";
        String s = str.substring(0, 9);
        String s2 = str2.substring(0, 9);
        System.out.println(s);
        System.out.println(s2);*/

        /*boolean validDate = isDate("2020/07/a1");
        System.out.println(validDate);

        StringBuffer stringBuffer = new StringBuffer();

        String choosedGroupFieldStr = thirdFieldList.stream().map(e -> stringBuffer.append(e)).collect(Collectors.joining(","));

        System.out.println(stringBuffer.toString());
        System.out.println(choosedGroupFieldStr);*/

        /*false
        cityregionsub_regioncountry
        city,cityregion,cityregionsub_region,cityregionsub_regioncountry*/

        /*String s = "2020/3/3".replace("/", "-");
        System.out.println(s);*/

        /*var str = "0";
        var repeated = str.repeat(3);
        System.out.println(repeated);*/

        /*var list = new ArrayList<Demo>();
        for (int i = 2; i < 100 ; i++) {
            Demo demo = new Demo().builder().id(i).cardName("中国银行" + i).cardNumber(99999999555L + i).build();
            list.add(demo);
            if (i % 2 == 0) {
                Demo newDemo = new Demo().builder().id(i).cardName("中国银行" + (i-1)).cardNumber(99999999555L + i-1).build();
                list.add(newDemo);
            }
        }

        // 按id升序排列, 再按cardNumber降序排列
        long start = System.currentTimeMillis();
        list.stream().sorted(Comparator.comparing(Demo::getId).thenComparing(Demo::getCardNumber, Comparator.reverseOrder()));
        long end = System.currentTimeMillis();
        System.out.println((end-start));
        list.forEach(System.out::println);*/

        /*String str = "2018-07-05 12:24:12";
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(str, pattern);
        System.out.println(parse.toString());*/

        String timeStr = "2019-08-26 18:03:33";
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, timeDtf);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        System.out.println(date);

    }

    /**
     * 百分比转换
     */
    public static Double percentToDouble(String str) {
        return str.contains("%") ? Double.valueOf(str.trim().replace("%", "")) / 100 : Double.valueOf(str);
    }



    /**
     * 判断是否是yyyy-MM-dd日期格式
     */
    public static boolean isDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(strDate.replace("/", "-"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

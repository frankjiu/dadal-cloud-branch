package com.modules.sys.controller;

import com.google.common.collect.Lists;
import com.modules.sys.model.entity.Demo;
import jodd.util.StringUtil;
import org.thymeleaf.util.DateUtils;

import javax.sql.rowset.serial.SQLOutputImpl;
import java.lang.reflect.Field;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Pattern pattern = Pattern.compile("&#x([0-9a-f]+);?");

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

        /*String timeStr = "2019-08-26 18:03:33";
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, timeDtf);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        System.out.println(date);*/

        /*String str = "aaa(test)bbb(111)";
        String restr = getInnerStr(str);

        System.out.println(restr);*/

        /*StringBuffer stb = new StringBuffer();
        String s = null;
        stb.append(s);
        stb.append("aa");

        String s1 = stb.toString();
        System.out.println(s1);*/

        /*int a = 0;
        ++a;
        System.out.println(a);*/

        /*String statisticWayForCabin = "(sum(rpk_curr)/sum(ask_curr)-sum(rpk_curr_ly)/sum(ask_curr_ly))";

        statisticWayForCabin = getInnerStr(statisticWayForCabin);

        String[] statisticField = statisticWayForCabin.split("\\/|-");

        List<String> list = Arrays.asList(statisticField);
        System.out.println(list.size());*/

        /*Long start = 1620835200000L;

        LocalDate localDate = Instant.ofEpochMilli(start).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        //LocalDateTime localDateTime = Instant.ofEpochMilli(start).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

        System.out.println(localDate);*/

        /*Date date = new Date();
        System.out.println(date);
        LocalDate localDate = dateToLocalDate(date);
        System.out.println(localDate);*/



        // sum(case when cabin in ('Y') then rpk_curr else 0 end) / sum(case when cabin in ('Y') then ask_curr else 0 end) as rlf,
        //String statisticWay = "sum(case when cabin in ('Y') then ask_curr else 0 end)";
        /*String statisticWay = "sum(rpk_curr)/sum(ask_curr)";
        String leftStr = "sum(case when cabin in ('Y') then ";
        String rightStr = " else 0 end)";

        // 正则表达式, 匹配字符串中的sum关键字, 匹配到sum关键字的取sum后括号内的字段, 对该字段添加case when条件.
        Matcher matcher = Pattern.compile("sum\\([a-z]{1,8}+_[a-z]+\\d?\\)").matcher(statisticWay);*/
        // 匹配sum区间段
        // String last = "";
        /*while (matcher.find()) {
            String sumStr = matcher.group(0);
            System.out.println(sumStr);
            String innerField = sumStr.substring(4, sumStr.length()-1);
            System.out.println(innerField);

            // 拼接case when条件
            String newSumStr = leftStr + innerField + rightStr;
            System.out.println(newSumStr);

            // 替换新的sum区间段
            String last = matcher.replaceFirst(newSumStr);
            System.out.println("===============" + last);

            //String last = statisticWay.replaceFirst(sumStr,newSumStr);
            //System.out.println("===============" + last);

        }*/


        /*String line = "This order was placed for QT3000! OK?";
        String pattern = "\\(\\.*\\)\\(\d+)\\(\\.*\\)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
        } else {
            System.out.println("NO MATCH");
        }*/


        /*String statisticWay = "sum(rpk_curr)/sum(ask_curr)";
        //String s = conditionStatistic(statisticWay);
        StringBuffer cabinClassCondition = new StringBuffer("cabin in ('Y') and seg_class in ('Y', 'P', 'B')");
        String s = conditionStatistic(statisticWay, cabinClassCondition);
        System.out.println(s);*/



        /*String statisticWay = "sum(seg_revenue_yqyr_curr)/sum(tkd_rpk_curr)/(sum(seg_revenue_yqyr_curr_ly)/sum(tkd_rpk_curr_ly))-1";
        String leftStr = "sum(case when cabin in ('Y') then ";
        String rightStr = " else 0 end)";

        String last = statisticWay;
        // 正则表达式, 匹配字符串中的sum关键字, 匹配到sum关键字的取sum后括号内的字段, 对该字段添加case when条件.
        Matcher matcher = Pattern.compile("sum\\([a-z]{1,8}+(_[a-z]+)+\\d?\\)").matcher(statisticWay);
        // 匹配sum区间段
        while (matcher.find()) {
            String sumStr = matcher.group(0);
            //System.out.println(sumStr);
            String innerField = sumStr.substring(4, sumStr.length()-1);
            //System.out.println(innerField);

            // 拼接case when条件
            String newSumStr = leftStr + innerField + rightStr;
            //System.out.println(newSumStr);

            // 替换新的sum区间段
            last  = last.replace(sumStr,newSumStr);
        }
        System.out.println("===============" + last);*/

        /*String s = "abc";
        int i = s.indexOf("a");
        System.out.println(i);*/

        /*String status = false == true ? "3" : "4";
        System.out.println(status);*/
        /*boolean flag = hasField(Demo.class, "cardName");
        System.out.println(flag);*/

        Demo demo = new Demo();
        demo.setCardName(null);

        String cardNameVal = getFieldValueByName("cardName", demo);
        System.out.println(cardNameVal);

    }

    private static String getFieldValueByName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到属性值
     * @param obj
     */
    public static void readAttributeValue(Object obj){
        String nameVlues="";
        //得到class
        Class cls = obj.getClass();
        //得到所有属性
        Field[] fields = cls.getDeclaredFields();
        for (int i=0;i<fields.length;i++){//遍历
            try {
                //得到属性
                Field field = fields[i];
                //打开私有访问
                field.setAccessible(true);
                //获取属性
                String name = field.getName();
                //获取属性值
                Object value = field.get(obj);
                //一个个赋值
                nameVlues += field.getName()+":"+value+",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //获取最后一个逗号的位置　　　　　　　
        int lastIndex = nameVlues.lastIndexOf(",");
        //不要最后一个逗号","
        String  result= nameVlues.substring(0,lastIndex);
        System.out.println(result);
    }

    public static boolean hasField(Class clazz, String fieldname) {
        boolean flag = false;
        Field[] fields = clazz.getDeclaredFields();
        for ( int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldname)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String conditionStatistic(String statisticWay){
        // statisticWay = "sum(rpk_curr)/sum(ask_curr)";
        String leftStr = "sum(case when cabin in ('Y') then ";
        String rightStr = " else 0 end)";
        // 正则表达式, 匹配字符串中的sum关键字, 匹配到sum关键字的取sum后括号内的字段, 对该字段添加case when条件.
        Matcher matcher = Pattern.compile("sum\\([a-z]{1,8}+(_[a-z]+)+\\d?\\)").matcher(statisticWay);
        // 匹配sum区间段
        while (matcher.find()) {
            String sumStr = matcher.group(0);
            String innerField = sumStr.substring(4, sumStr.length()-1);
            // 拼接case when条件
            String newSumStr = leftStr + innerField + rightStr;
            // 替换新的sum区间段
            statisticWay  = statisticWay.replace(sumStr,newSumStr);
        }
        return statisticWay;
    }

    public String conditionStatistic1(String statisticWay){
        //String statisticWay = "sum(rpk_curr)/sum(ask_curr)";
        statisticWay = "sum(seg_revenue_yqyr_curr)/sum(tkd_rpk_curr)/(sum(seg_revenue_yqyr_curr_ly)/sum(tkd_rpk_curr_ly))-1";
        String leftStr = "sum(case when cabin in ('Y') then ";
        String rightStr = " else 0 end)";

        String last = statisticWay;
        // 正则表达式, 匹配字符串中的sum关键字, 匹配到sum关键字的取sum后括号内的字段, 对该字段添加case when条件.
        Matcher matcher = Pattern.compile("sum\\([a-z]{1,8}+(_[a-z]+)+\\d?\\)").matcher(statisticWay);
        // 匹配sum区间段
        while (matcher.find()) {
            String sumStr = matcher.group(0);
            //System.out.println(sumStr);
            String innerField = sumStr.substring(4, sumStr.length()-1);
            //System.out.println(innerField);

            // 拼接case when条件
            String newSumStr = leftStr + innerField + rightStr;
            //System.out.println(newSumStr);

            // 替换新的sum区间段
            last  = last.replace(sumStr,newSumStr);
        }
        return last;
    }

    /**
     * question
     */
    private static String conditionStatistic(String statisticWay, StringBuffer innerCabinClassCondition){
        // statisticWay = "sum(rpk_curr)/sum(ask_curr)";
        // 正则表达式, 匹配字符串中的sum关键字, 匹配到sum关键字的取sum后括号内的字段, 对该字段添加case when条件.
        Matcher matcher = Pattern.compile("sum\\([a-z]{1,8}+(_[a-z]+)+\\d?\\)").matcher(statisticWay);
        // 匹配sum区间段
        while (matcher.find()) {
            StringBuffer statisticWayBuffer = new StringBuffer();
            String sumStr = matcher.group(0);
            String innerField = sumStr.substring(4, sumStr.length()-1);
            // 拼接case when条件
            String newSumStr = statisticWayBuffer.append("sum(case when ")
                    .append(innerCabinClassCondition)
                    .append(" then ")
                    .append(innerField)
                    .append(" else 0 end)").toString();
            // 替换新的sum区间段
            statisticWay  = statisticWay.replace(sumStr,newSumStr);
        }
        return statisticWay;
    }




    public static LocalDate dateToLocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String getInnerStr(String str) {
        return str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
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

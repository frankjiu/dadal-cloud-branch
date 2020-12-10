package com;

import com.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-24
 */
public class MixedTest {

    public static final List<String> thirdFieldList = new ArrayList<>() {{
        add("city");
        add("region");
        add("sub_region");
        add("country");
    }};

    private static final Pattern pattern = Pattern.compile("&#x([0-9a-f]+);?");

    //private static OkHttpClient client;

    @Test
    public void testMe1() {
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

        /*Demo demo = new Demo();
        demo.setCardName(null);

        String cardNameVal = getFieldValueByName("cardName", demo);
        System.out.println(cardNameVal);*/

        /*String test = "BOX_SSTXFFABDK";
        String s = test.substring(0, 4);
        System.out.println(s);*/

        /*List<String> oriCityList = new ArrayList<>();
        oriCityList.add("A");
        oriCityList.add("B");
        List<String> destCityList = new ArrayList<>();
        destCityList.add("1");
        destCityList.add("2");
        destCityList.add("3");

        for (String ori : oriCityList) {
            for (String dest : destCityList) {
                String origin = ori;
                String destination = dest;
                String total = origin + "-" + destination;
                System.out.println(total);
            }

        }*/

        /*User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setName("zhangsan");
        user1.setAddress("beijing");
        user1.setAge(10);
        user2.setName("zhangsan");
        user2.setAddress("beijing");
        user2.setAge(20);
        user3.setName("lisi");
        user3.setAddress("shanghai");
        user3.setAge(30);
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        Map<String, List<User>> collect = list.stream().collect(Collectors.groupingBy(e -> getGroupField(e)));
        Set<String> set = collect.keySet();
        set.forEach(e -> System.out.println(e)); // zhangsan#beijing    lisi#shanghai
        User user = new User();
        User.Detail detailList = new User.Detail();
        collect.entrySet().stream().map(e -> {
            String key = e.getKey();
            user.setName(key);
            user.setAddress(key);
            return key;
        });*/

        //Map<String, List<User>> collect = list.stream().collect(Collectors.groupingBy(User::getName));


        // {zhangsan#beijing=[User{age=10, name='zhangsan', address='beijing'}, User{age=20, name='zhangsan', address='beijing'}],
        // lisi#shanghai=[User{age=30, name='lisi', address='shanghai'}]}

        //System.out.println(collect);

        //LocalDateTime now = LocalDateTime.now();
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        System.out.println(now);*/

        /*String a = "[\"R\",\"S\",\"T\",\"E\",\"V\",\"G\",\"X\",\"I\",\"Z\",\"N\",\"O\"]";
        String substring = a.substring(1, a.length() - 1).replace("\"","");
        System.out.println(substring);*/

        /*Integer s = null;
        String a = s + "aaa";
        System.out.println(a);*/

        //long dateTime = 148306827694L;
        //Date date = new Date(dateTime);
        //System.out.println(date.toString());

        /*Date depDateTimeLeft = new Date("1970/1/1");
        // Date parse1 = DateFormat.parse("1970-01-01");
        System.out.println(depDateTimeLeft);*/

        /*List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        List<String> strings = list.subList(1, 3); // 包首不包尾
        for (String string : strings) {
            System.out.println(string);
        }

        String a = null;
        String b = a + "ss";
        System.out.println(b);*/

        /*Integer integer = Integer.valueOf("");
        System.out.println(integer);*/
        /*List<String> a = new ArrayList<>();
        String join = String.join(",", a);
        System.out.println(join);*/
        //String patternFirst = null;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFirst);

        /*Assert.notNull(patternFirst, "patternFirst is required!");

        // String类型转LocalDate
        String str = "2020-11-11 15:41:40";
        String formattering = "yyyyMMddHHmmss";
        //String formattering = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime localDateTime = string2LocalDateTime(str.replaceAll("\\s|-|:", ""), formattering);
        System.out.println(localDateTime);
        String s = str.replaceAll("\\s|-|:", "");
        System.out.println(s);*/

        /*Pattern compile = Pattern.compile("(^$)|(^[1-9]\\d*)|(0$)");
        boolean flag = compile.matcher("0").find();
        System.out.println(flag);*/
        /*BigDecimal finalLySeatShare = null;
        if (finalLySeatShare == null) {
            finalLySeatShare = BigDecimal.ZERO;
        }
        //boolean a = finalLySeatShare.compareTo(new BigDecimal(0)) == -1;
        boolean a = new BigDecimal(-0.1).compareTo(finalLySeatShare) == -1;
        System.out.println(a);*/

        /*Boolean f = null;
        if ("true".equalsIgnoreCase(f+"")) {
            System.out.println("eeeee");
        }
        System.out.println(null + "eeeee");
        //System.out.println("eeeee".indexOf(null));

        Assert.notNull("", "HomeMarket1对象不能为空!");
        Assert.notNull(null, "HomeMarket2对象不能为空!");*/

        /*String str1 = "2015-02-08 20:20:20";
        String str2 = null;
        int res = str1.compareTo(str2);
        if (res > 0)
            System.out.println("str1>str2");
        else if (res == 0)
            System.out.println("str1=str2");
        else
            System.out.println("str1<str2");*/

        /*List<String> dateStartList = new ArrayList<>();
        dateStartList.add("2020/05/09");
        dateStartList.add("");
        dateStartList.add("2030/01/09");
        dateStartList.add("2015/03/09");
        dateStartList.add("2019/04/09");

        dateStartList.forEach(str -> System.out.println(str));
        System.out.println("======================");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Collections.sort(dateStartList, (s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));
        dateStartList.forEach(str -> System.out.println(str));

        dateStartList.sort(Comparator.comparing((String s) -> s).reversed());
        dateStartList.forEach(str -> System.out.println(str));*/

        /*List<String> strDows = new ArrayList<>();
        strDows.add("2");
        strDows.add("5");
        strDows.add("8");
        String join = String.join("", strDows);
        System.out.println(join);*/

        /*//StringUtils
        String s = org.apache.commons.lang3.StringUtils.leftPad("10", 4, "0"); // 0010
        System.out.println(s);

        String abcde = org.apache.commons.lang3.StringUtils.left("ABCDE", 3);
        System.out.println(abcde);

        //如果字符串长度小于参数二的值，首部加空格补全。(小于字符串长度不处理返回)
        org.apache.commons.lang3.StringUtils.leftPad("海川", 4); //   海川*/

        /*try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //findDataByOkHttp();

        /*HttpClient client = new HttpClient().execute()
        client.getState().setCredentials("www.cnblogs.com","user/signin",new UsernamePasswordCredentials("username", "password"));
        GetMethod method = new GetMethod("http://home.cnblogs.com/u/Sir-Lin/followers/1/");
        method.setDoAuthentication(true);
        int status = client.executeMethod(method);
        method.releaseConnection();*/

        /*String marketId = "AIRPORT_AIRPORT_CAN_PKX";
        String org = marketId.substring(marketId.length() - 3);
        String des = marketId.substring(marketId.length() - 7, marketId.length() - 4);
        String lvId = marketId.substring(0, marketId.length() - 8);*/

        /*Integer a = null;
        Boolean f = a > 3;
        System.out.println(f); // NPE */

        /*List<String> list = Arrays.asList("a,b,c", "b,c,e", "a,d,f", "a,e", "b,d");
        Set<String> sets = handle(list);
        list.stream().forEach(e -> System.out.println(e));*/


        /*ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }

        long l1 = System.currentTimeMillis();
        LongAdder sum = new LongAdder();
        list.parallelStream().forEach(integer -> {
        //System.out.println("当前线程" + thread.currentThread().getName());
            sum.add(integer);
        });
        long l2 = System.currentTimeMillis();
        System.out.println(sum + ":" + (l2-l1));

        long l3 = System.currentTimeMillis();
        LongAdder sum2 = new LongAdder();
        list.stream().forEach(e -> sum2.add(e));
        long l4 = System.currentTimeMillis();
        System.out.println(sum2 + ":" + (l4 - l3));*/

        //jdk11 httpclient:  java.net.http. 包
        /*HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.weather.com.cn/weather/101280101.shtml"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());*/

        // 方式一
        /*String userName = "admin";
        String passWord = "rtdpTest@2019";
        Integer REQUEST_TIMEOUT_MS = 15;
        String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/competitiveStrategy";
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_MS))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, passWord.toCharArray());
                    }
                }).build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json").uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());*/

        /*// 方式二
        String url = "https://www.weather.com.cn/weather/101280101.shtml";
        URL u = new URL(url);
        if("https".equalsIgnoreCase(u.getProtocol())){
            SslUtils.ignoreSsl();
        }
        URLConnection conn = u.openConnection();
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        String s = IOUtils.toString(conn.getInputStream());
        System.out.println(s);*/

        /*List<Demo> dtoList = new ArrayList<>();
        Demo demo1 = new Demo();
        demo1.setCardName("FBC623");
        Demo demo2 = new Demo();
        demo2.setCardName("BBC321");
        Demo demo3 = new Demo();
        demo3.setCardName("ABC613");
        Demo demo4 = new Demo();
        demo4.setCardName("ECB123");
        Demo demo5 = new Demo();
        demo5.setCardName("ABC283");
        Demo demo6 = new Demo();
        demo6.setCardName("ABC281");
        Demo demo7 = new Demo();
        demo7.setCardName("CAB512");
        dtoList.add(demo1);
        dtoList.add(demo2);
        dtoList.add(demo3);
        dtoList.add(demo4);
        dtoList.add(demo5);
        dtoList.add(demo6);
        dtoList.add(demo7);
        //Collections.sort(dtoList, (s1, s2) -> s1.getCardName().compareTo(s2.getCardName()) > 0 ? 1 : -1);
        Collections.sort(dtoList, (s1, s2) -> s1.getCardName().compareTo(s2.getCardName()) > 0 ? -1 : 1); // 倒序
        dtoList.stream().forEach(e -> System.out.println(e.getCardName()));*/

        // 记录end值比start大, 并且记录start值比end小, 即通过
        /*String recordStart = "2020-04-01";
        String recordEnd = "2020-06-30";
        String conditionStart = "20201116";
        String conditionEnd = "20200402";
        Boolean flag = false;
        if (conditionEnd.compareTo(conditionStart) > 0) {
            flag = true;
        }
        System.out.println(flag);*/

        /*List<Stu> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Stu stu = new Stu();
            stu.setId(i+"");
            stu.setAge(i);
            list.add(stu);
        }

        long sum = list.stream().map(e -> e.getAge()).mapToLong(e -> e.longValue()).sum();
        System.out.println("sum: " + sum);*/

        //System.out.println(Long.MAX_VALUE); // 922 3372 0368 5477 5807

        /*Long a = 10L;
        Long b = 12L;

        Long c = a + b;
        // System.out.println((a + b) instanceof Long);
        //下面这句就可以，因为上面用Long去接收a + b的值了，就又装箱了。
        System.out.println(c instanceof Long);
        System.out.println(c);

        BigDecimal x = new BigDecimal("0");
        long val = x.longValue();
        System.out.println(x);
        System.out.println(val);*/

        /*StringBuffer stringBuffer = new StringBuffer();
        System.out.println(stringBuffer.toString());*/

        /*int a = 200;
        Boolean flag = true;
        if (!(a == 200 || a == 201)) {
            flag = false;
        }*/

        /*Set<String> list = new HashSet<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println(list.toString());
        //System.out.println(org.apache.commons.lang3.StringUtils.strip(list.toString(),"[]"));
        String[] strArr = new String[3];
        System.out.println(strArr);

        Map<String, Object> map = new HashMap<>();
        map.put("x", 1L);
        map.put("y", 2L);
        map.put("z", 3L);
        System.out.println(map);*/

        /*List<Integer> list=Arrays.asList(0,1);
        System.out.println(list.toString());

        StringBuilder stb = new StringBuilder();
        list.forEach(stb::append);
        System.out.println(stb.toString());*/

        //java9引入 List.of("a","b"); 生成一个List<String> 后,集合元素就是不可变的了,如果再增加list中的元素就会报错
        /*List<Integer> list = List.of(1, 2);
        System.out.println(list.size());
        Integer integer = list.get(1);
        System.out.println(integer);*/

        /*List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        List<String> collect = list.stream().map(e -> e).collect(Collectors.toList());// 使用集合创建流

        List<String> collect1 = Stream.of("a", "c", "b").collect(Collectors.toList()); // Stream.of("a", "c", "b"),使用指定元素创建有序流Stream
        System.out.println(collect1);*/

        //Stream<String> stream = Stream.of("a", "c", "b");

        /*Stream<String> stream = Stream.of("1;", "2;", "3;", "4;");
        // 使用Lambda语法： s.reduce("0;", (s1, s2) -> s1.concat(s2));
        String str = stream.reduce("0;", new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s.concat(s2);
            }
        });
        System.out.println(str); // 0;1;2;3;4;*/

        /*Integer i = Stream.of(1, 2, 3).parallel().reduce(4, (s1, s2) -> s1 + s2
                , (s1, s2) -> s1 + s2);
        System.out.println(i); // 18*/

        /*Demo demo = new Demo();
        demo.setCardName("haha");
        // PropertyUtils.setProperty(demo, "test", "abc");
        System.out.println(demo);

        DynaProperty testField = new DynaProperty("testField", String.class);
        BasicDynaClass dynaDemo = new BasicDynaClass("demo", null, new DynaProperty[]{testField});
        BasicDynaBean dynaDemoBean = new BasicDynaBean(dynaDemo);
        BeanUtils.copyProperties(dynaDemoBean, demo);
        dynaDemoBean.set("testField", "aaaaaaaaaaaa");

        Map<String, Object> map = dynaDemoBean.getMap();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) { String key = it.next();
            System.out.println(key + ":" + map.get(key));
        }*/

        /*SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = formatter.parse("20110305");
        System.out.println(date);*/

        /*Date date1 = new SimpleDateFormat("yyyyMMdd").parse("20110305");
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date1);
        System.out.println(dateString);*/

        /*int[] arr = new int[2];
        arr[0] = 3;
        arr[1] = 5;
        SumTask sumTask = new SumTask(1, 10, arr);
        Integer integer = sumTask.get();
        System.out.println(integer);*/

        /*MyTask myTask = new MyTask(0,100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        System.out.println(forkJoinTask.get());
        forkJoinPool.shutdown();

        long l = Runtime.getRuntime().freeMemory();
        long l1 = Runtime.getRuntime().totalMemory();
        System.out.println(l/1024L/1024L);
        System.out.println(l1/1024L/1024L);*/

        /*Object a = "aa";
        Object b = "aa";
        boolean b1 = a.toString().equals(b.toString().toUpperCase());
        System.out.println(b1);*/

        /*List<String> posValue = new ArrayList<>();
        posValue.add("AU");
        posValue.add("AS");
        posValue.add("AT");
        String str = "*" + String.join(",", posValue);
        System.out.println(str);*/

        /*Optional<Integer> canBeEmpty1 = Optional.of(5);
        canBeEmpty1.isPresent();                    // returns true
        canBeEmpty1.get();                          // returns 5

        Optional<Integer> canBeEmpty2 = Optional.empty();
        canBeEmpty2.isPresent();                    // returns false
        Integer i = canBeEmpty2.get();
        System.out.println(i);*/

        //假设此值已从方法返回
        //Optional<Demo> companyOptional = Optional.of(new Demo());
        /*Optional<Demo> companyOptional = Optional.empty();

        // 使用静态工厂方法 Optional.ofNullable()构建一个可能为null的Optional对象
        Demo demo = new Demo("test121");
        Optional<Demo> optDemo = Optional.ofNullable(demo);
        // 打印出 cardName.
        optDemo.ifPresent(e -> System.out.println(e.getCardName()));
        // 合并起来
        Optional.ofNullable(demo).ifPresent(e -> System.out.println(e.getCardName()));
        // 如果存在满足条件的数据打印出来
        optDemo.filter(e -> "test121".equals(e.getCardName())).ifPresent((e) -> System.out.println("abc"));
        // 取出 cardName:  orElse(如果存在则返回原值, 否则返回新值)
        // demo = null;
        demo = new Demo();
        String cardName = Optional.ofNullable(demo.getCardName()).orElse("new"); //demo不为空打印原值; 否则打印新对象的值;
        System.out.println("===========" + cardName);*/

        /*boolean ff = org.apache.commons.lang3.StringUtils.equals(null, null); //比较: 都是null也返回true
        boolean b = org.apache.commons.lang3.StringUtils.containsAny("abcabc", "de");// false
        boolean b1 = org.apache.commons.lang3.StringUtils.containsOnly("abcabc", "abc");//true
        int i = org.apache.commons.lang3.StringUtils.indexOfIgnoreCase("ABc", "a"); //0
        String str = org.apache.commons.lang3.StringUtils.left("abcabc", 4);//abca
        String str1 = org.apache.commons.lang3.StringUtils.right("abcabc", 4);//cabc
        String str2 = org.apache.commons.lang3.StringUtils.mid("abcabc", 0,2);//ab
        String str3 = org.apache.commons.lang3.StringUtils.leftPad("123", 6,"0");//000123
        String str4 = org.apache.commons.lang3.StringUtils.chop("abc");//去掉字符串的最后一个字符:ab
        boolean f = org.apache.commons.lang3.StringUtils.isAlpha("abcdefg");//判断字符串是否都是字母 true
        boolean f1 = org.apache.commons.lang3.StringUtils.isNumeric("12345");//判断字符串是否都是数字 true
        String str5 = org.apache.commons.lang3.StringUtils.reverse("12345");//翻转 54321
        String str6 = org.apache.commons.lang3.StringUtils.abbreviate("now i will say a story!", 7);//省略文字如:now ...
        String[] split = org.apache.commons.lang3.StringUtils.split("a-b-c", "-"); //使用字符串作为分割参数
        //ArrayUtils类*/

        /*List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        boolean b = list1.addAll(list2);
        System.out.println(b);*/

        /*System.out.println(LocalDateTime.now());
        String abc = org.apache.commons.lang3.StringUtils.capitalize("abc");
        System.out.println(abc);
        boolean numeric = org.apache.commons.lang3.StringUtils.isNumericSpace("34 5");
        System.out.println(numeric);

        Date date1 = new Date();
        System.out.println(date1);

        String date = DateFormatUtils.format(1607388261406L, "yyyy-MM-dd");
        System.out.println(date);

        String dateStr = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(1607388261406L);
        System.out.println(dateStr);

        String format = DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(1607388261406L);
        System.out.println(format);

        Demo demo = new Demo("hahah");
        System.out.println(ToStringBuilder.reflectionToString(demo));

        //CollectionUtils.union()

        ArrayList<Object> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(4);
        list1.add(5);
        ArrayList<Object> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(7);

        Collection<Object> lists = CollectionUtils.subtract(list2, list1);
        System.out.println(lists);

        Date date2 = DateUtils.addYears(new Date(), 2);
        System.out.println(date2);

        Date date3 = DateUtils.parseDate("1998-05-06", "yyyy-MM-dd");
        System.out.println(date3);

        boolean f1 = NumberUtils.isDigits("3a");
        System.out.println(f1);

        String path = "C:\\Users\\Administrator\\Desktop\\RedisConfig.java";
        String s1 = FilenameUtils.getBaseName(path);
        System.out.println(s1);

        String s2 = FilenameUtils.getExtension(path);        //获取扩展名
        System.out.println(s2);

        String s3 = FilenameUtils.separatorsToUnix(path);    //转成 linux 路径分隔符
        System.out.println(s3);

        byte[] bt = new byte[]{1, 2};
        byte[] bytes = Base64.encodeBase64(new byte[]{1, 2});
        System.out.println(new String(bytes));
        byte[] bytes1 = Base64.decodeBase64(bytes);
        System.out.println(new String(bytes1));

        char[] chars = Hex.encodeHex(bt);
        System.out.println(new String(chars));

        System.out.println(RandomUtils.nextInt(10, 20));

        String s = Base64Utils.encodeToString("www.baidu.com".getBytes());
        System.out.println(s);

        byte[] bytes2 = Base64Utils.decodeFromString(s);
        String s4 = new String(bytes2);
        System.out.println(s4);

        String s5 = java.utils.Base64.getEncoder().encodeToString("".getBytes(StandardCharsets.UTF_8));*/

        /*String s = KeyUtil.generateUniqueKey();
        System.out.println(s);

        String salt = RandomStringUtils.randomAlphanumeric(20);
        System.out.println(salt);*/
    }

    @Test
    public void testMe2() throws JsonProcessingException {
        /*Random random = new Random();
        int i = random.nextInt(2);
        System.out.println(i);*/

        /*User user = new User("laowang", 25, List.of(
                new User.Detail("详细地址1", "广州白云区"),
                new User.Detail("详细地址2", null),
                new User.Detail("详细地址3", "广州越秀区")
        ));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.valueToTree(user);
        JsonNode detailsNodes = rootNode.get("details");
        Iterator<JsonNode> iterator = detailsNodes.iterator();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            String address = next.get("address").asText();
            if (isEmptyNull(address)) {
                System.out.println("=========address属性为空========");
                ObjectNode innerNode = (ObjectNode)next;
                innerNode.remove("address");
                iterator.remove(); // 移除节点数组中当前空元素
            }
        }
        System.out.println("<<<rootNode: " + rootNode);*/

    }

    @Test
    public void testMe3() throws Exception {

    }

    public boolean isEmptyNull(Object str) {
        return (str == null || "".equals(str) || "null".equals(str));
    }


    static class MyTask extends RecursiveTask<Integer>{
        private final Integer ADJUST_VALUE = 10;
        private int begin;
        private int end;
        private int result;

        public MyTask(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if((end - begin)<=ADJUST_VALUE){
                for(int i =begin;i <= end;i++){
                    result = result + i;
                }
            }else{
                int middle = (begin + end)/2;
                MyTask task01 = new MyTask(begin,middle);
                MyTask task02 = new MyTask(middle+1,end);
                task01.fork();
                task02.fork();
                result =  task01.join() + task02.join();
            }


            return result;
        }
    }

    public static Set<String> handle(List<String> list) {
        return Optional.ofNullable(list)//判空
                .orElse(new ArrayList<>())//空处理
                .stream()//转流操作
                .map(str -> str.split(","))//将列表单个元素分离，得到数组
                .flatMap(Arrays::stream)//将上一步得到的数组转流，并拼接成一个stream
                .collect(Collectors.toSet());//将流转Set集合，去重
    }

    /*public static void findDataByOkHttp() throws IOException {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(30, TimeUnit.SECONDS);
        //代理服务器的IP和端口号  https://172.28.129.6:9990
        //172.28.129.6:9990
        //admin/rtdpTest@2019
        String userName = "admin";
        String passWord = "rtdpTest@2019";
        Integer REQUEST_TIMEOUT_MS = 15;
        //String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/{marketId}/competitiveStrategy/{strategyId}";
        String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/AIRPORT_AIRPORT_CAN_USA/competitiveStrategy?id=AIRPORT_AIRPORT_CAN_USA_817d2651-39a6-496d-ad31" +
                "-729856987043";
        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.28.129.6", 9990)));
        //代理的鉴权账号密码
        builder.proxyAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) {
                //设置代理服务器账号密码
                String credential = Credentials.basic(userName, passWord);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .addHeader("Connection", "close")
                        .build();
            }
        });
        *//*client = builder
                //设置读取超时时间
                .readTimeout(REQUEST_TIMEOUT_MS, TimeUnit.SECONDS)
                //设置写的超时时间
                .writeTimeout(REQUEST_TIMEOUT_MS, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT_MS, TimeUnit.SECONDS).build();*//*
        client = builder.build();
        ////////////////////////////////////////////////////////////////////////////

        //String url = ipPrefix + uriPrefix + competitiveGetIdUrl;
        //url = url.replace("{marketId}", params.get("marketId")).replace("/{strategyId}", "?id=" + params.get("strategyId"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        Response response = client.newCall(request).execute();
        // String str = OkHttp3Util.get(url, params);
        ResponseBody body = response.body();
        System.out.println(body.toString());
    }

    public static void run() throws Exception {
        //authenticate();
        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }*/


    /**
     * String类型转LocalDate
     * @param strDate
     * @param pattern
     * @return
     */
    public static LocalDate string2LocalDate(String strDate, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = LocalDate.parse(strDate, formatter);
        return localDate;
    }

    /**
     * String类型转LocalDateTime
     * @param strDate
     * @param pattern
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String strDate, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
        return localDateTime;
    }

    private static String getGroupField(User user){
        return user.getName() +"#"+ user.getAge();
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

/*@Data
class Stu {
    private String id;
    private Integer age;
}*/

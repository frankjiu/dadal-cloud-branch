package com;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.modules.base.model.entity.AliRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.management.LockInfo;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Autowired
    RedisTemplate redisTemplate;

    public String str;
    public int a;

    @Test
    public void test1() throws IOException, InterruptedException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {

        //List<Integer> numList = Lists.newArrayList(0, 0, 0, 1, 2, 3, 4, 5, 6, 0, 7, 8, 9, 0, 0, 0, 0, 0); // 18
        List<Integer> numList = Lists.newArrayList(1, 0); // 18
        /*List<List<Integer>> lists=Lists.partition(numList,4);
        lists.get(0).remove(2);
        System.out.println(numList);//[[1, 2, 3], [4, 5, 6], [7, 8]]

        Map.Entry<String,String> entry = new HashMap.SimpleEntry<>("a", "1");
        System.out.println(entry);
        System.out.println("getKey:" + entry.getKey());
        System.out.println("getValue:" + entry.getValue());
        entry.setValue("2");
        System.out.println("setValue:" + entry);

        Long date = 1601481600L;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String stringDate = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(date*1000), ZoneId.systemDefault()));
        System.out.println(stringDate);*/

        /*int a = 3;
        System.out.println(1.03-0.42);

        int left = 0;
        int right = 0;
        for (int i = 0; i <numList.size(); i++) {
            if (numList.get(i) != 0) {
                left = i;
                break;
            }
        }

        for (int i = numList.size()-1; i >= 0; i--) {
            if (numList.get(i) != 0 && numList.size() > 0) {
                right = i + 1;
                break;
            }
        }
        List<Integer> newList = numList.subList(left, right);
        System.out.println(newList);*/

        /*Integer a = 300938;
        Integer b = 418861;
        Integer c = 0;
        Integer d = 0;
        Integer s = b == 0 || c == 0 ? null : (a/b)/(c/d) - 1;
        System.out.println(s);*/

        /*User user = new User();
        user.setId(305L);
        user.setUserName("frank");
        user.setPassWord("123456");
        user.setEmail("2309094456@qq.com");
        user.setMobile("18553517590");


        BigDecimal a = new BigDecimal(0.00);
        int compare = a.compareTo(BigDecimal.ZERO);
        System.out.println(compare);*/

        /*Map<String, String> m = new HashMap(3);
        m.put("a", "b");
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(m);
        System.out.println(table == null ? ">>>>>>>>>>>>" + 0 : ">>>>>>>>>>>>" + table.length);

        Map<String, String> map = new HashMap(3);
        map.put("a", "b");
        Method capacity = map.getClass().getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity: " + capacity.invoke(map));

        int a = (int) ((float) 3 / 0.75F + 1.0F);
        System.out.println("............." + a);*/


        /*String a = "hello";
        String b = a;
        a = a + "world";
        System.out.println(a);
        System.out.println(b);*/

        /*Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int a = random.nextInt(100);
            System.out.println(a);
        }*/

        /*int compare = Integer.compare(3, 5);

        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(4);
        list.add(1);
        list.add(9);
        Integer min = Collections.min(list);
        System.out.println(min);

        System.out.println(str);
        System.out.println(a);

        Class<SimTest> simTestClass = SimTest.class;*/

        /*QRCodeUtil qrCodeUtil = new QRCodeUtil();
        String ab = qrCodeUtil.crateQRCode("ab");
        System.out.println(ab);*/

        /*String encodeStr = DigestUtils.md5Hex("ABD");
        System.out.println(encodeStr);
        String decodeStr = new String(DigestUtils.md5(encodeStr), "UTF-8");
        System.out.println(decodeStr);

        String encoded = Base64.getEncoder().encodeToString(DigestUtils.md5(encodeStr));
        System.out.println(encoded);*/

        //System.out.println("aa".equals(null));
        //long l = System.currentTimeMillis();
        //System.out.println(l); //162 452 1413 337

        /*String fileName = "abc.jpg";
        String[] suffixArr = {"jpg", "png", "jpeg", "bmp"};
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (Arrays.asList(suffixArr).contains(suffix)) {
            System.out.println(suffix);
        }*/

        /*UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String fileName = "abc.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        System.out.println(suffix);*/

        /*long time = new Date().getTime();
        long l = System.currentTimeMillis();
        System.out.println(time); // 162 460 2098 864
        System.out.println(l);*/

        /*String str = "alipay_sdk=alipay-sdk-java-4.8.103.ALL&app_id=2021002145607382&biz_content=%7B%22out_trade_no%22%3A%221409749588704890880%22%2C%22total_amount%22%3A%22100" +
                ".00%22%2C%22subject%22%3A%22null%E5%85%85%E5%80%BC+100.00+%E5%85%83%E5%A5%96%E5%8A%B1+20" +
                ".00+%E5%85%83%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&sign=OTqiqMiOo3clmCIxZEcfbqRtWZ7x7c3S1LwcSQY5YTUvsvY0TFGg%2FVwTeoBwLcgTHUfjD5Bmfo9iQBRDv4LDLeCTl%2FKzoksqXYcVEPa66X%2BsyMolDX%2F0sndxEauwGS9a8ugwn3LUQlVzg%2FTMneElUQG5eR6IvCzS0myRcNB%2Bv1Gn%2FSFOW7B%2F8FtskHZDhAi3sRMnulG2P5n40yLxu9oYsrnjX4GqR%2F39kUWnhqJzOvZr9AMPKSICbDdMKqIOXiphvUOyF94O12Wj2XGaH8k5SB3zTYopnpTrSkfVzrMD9tyeJ849vuR2xUFwMYSc2aDaB08yYXYRBWUiLBMbNynh2w%3D%3D&sign_type=RSA2&timestamp=2021-06-29+13%3A44%3A28&version=1.0";
        String replaceStr = str.replace("&", "\",\"").replace("=", "\":\"");
        String data = "{\"" + replaceStr + "\"}";
        ObjectMapper mapper = new ObjectMapper();
        AliRes result = mapper.readValue(data, new TypeReference<AliRes>(){});
        Gson gson = new Gson();
        AliRes aliRes = gson.fromJson(data, AliRes.class);
        System.out.println(aliRes);*/

        /*boolean equals = "".equals(null);
        System.out.println(equals);*/

        /*Long id1 = 1258966264475422720L;
        Long id2 = 1258966264475422720L;
        boolean flag = id1.equals(id2);
        System.out.println(flag);*/

        /*System.out.println(">>>" + File.separator);
        System.out.println(">>>" + File.separatorChar);
        System.out.println(">>>" + File.pathSeparator);
        System.out.println(">>>" + File.pathSeparatorChar);*/

        Long a = 1406863105862012928L;
        Long b = 1406863105862012928L;
        boolean flag = a.toString().equals(b.toString());
        System.out.println(flag);

    }



}

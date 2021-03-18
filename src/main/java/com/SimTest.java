package com;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.modules.sys.admin.model.entity.User;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test1() throws IOException, InterruptedException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {

        //List<Integer> numList = Lists.newArrayList(0, 0, 0, 1, 2, 3, 4, 5, 6, 0, 7, 8, 9, 0, 0, 0, 0, 0); // 18
        List<Integer> numList = Lists.newArrayList(1,0); // 18
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

        int compare = Integer.compare(3, 5);

        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(4);
        list.add(1);
        list.add(9);
        Integer min = Collections.min(list);
        System.out.println(min);


    }



}

package com;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test1() throws IOException, InterruptedException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

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

        int a = 3;
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
        System.out.println(newList);

    }


}

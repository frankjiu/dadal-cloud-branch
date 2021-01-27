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

        List<Integer> numList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<List<Integer>> lists=Lists.partition(numList,4);
        lists.get(0).remove(2);
        System.out.println(numList);//[[1, 2, 3], [4, 5, 6], [7, 8]]

        Map.Entry<String,String> entry = new HashMap.SimpleEntry<>("a", "1");
        System.out.println(entry);
        System.out.println("getKey:" + entry.getKey());
        System.out.println("getValue:" + entry.getValue());
        entry.setValue("2");
        System.out.println("setValue:" + entry);


    }

    // 数据库层面的命令使用execute实现
    public void testFlushDB(){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }

}

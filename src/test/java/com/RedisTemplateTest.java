package com;

import com.modules.base.model.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CloudBranchApplication.class})
@Slf4j
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisTemplateTest() {
        RedisTemplate redisTemplate = new RedisTemplate();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        redisTemplate.opsForValue().set("a", "heiheihei!");
        Object a = redisTemplate.opsForValue().get("a");
        System.out.println(a);

        ValueOperations<String, Object> opr = redisTemplate.opsForValue();
        opr.set("demo", new Demo(55, "华夏", 8L, null));
        Demo demo = (Demo) opr.get("demo");
        redisTemplate.opsForValue().set("name", "tom", 10, TimeUnit.SECONDS);
        String name = (String) redisTemplate.opsForValue().get("name");//由于设置的是10秒失效，十秒之内查询有结果，十秒之后返回为null
        System.out.println(demo);
        System.out.println(name);

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("multi1", "multi1");
        maps.put("multi2", "multi2");
        maps.put("multi3", "multi3");
        redisTemplate.opsForValue().multiSet(maps);
        List<String> keys = new ArrayList<String>();
        keys.add("multi1");
        keys.add("multi2");
        keys.add("multi3");
        System.out.println(redisTemplate.opsForValue().multiGet(keys));

        redisTemplate.opsForValue().increment("increlong", 1); //执行一次自增一次
        System.out.println("***************" + redisTemplate.opsForValue().get("increlong"));

        List<Object> strings = new ArrayList<Object>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        redisTemplate.opsForList().rightPushAll("listcollectionright", strings);
        System.out.println(redisTemplate.opsForList().range("listcollectionright", 0, -1));

        System.out.println("randomMembers:" + redisTemplate.opsForSet().distinctRandomMembers("setTest", 5));

        redisTemplate.opsForList().leftPushAll("testList", "5", "8", "3", "9", "7");
        SortQuery<String> query = SortQueryBuilder.sort("testList")// 排序的key
                //.by("pattern")       key的正则过滤
                //.noSort()            //不使用正则过滤key
                //.get("#")            //在value里过滤正则，可以连续写多个get
                .limit(0, 8)         //分页，和mysql一样
                .order(SortParameters.Order.DESC)   //正序or倒序
                .alphabetical(true)  //ALPHA修饰符用于对字符串进行排序，false的话只针对数字排序
                .build();
        List sortList = redisTemplate.sort(query);
        System.out.println(sortList);


    }

}


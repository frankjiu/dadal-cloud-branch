package com;

import com.google.common.util.concurrent.RateLimiter;
import com.modules.sys.model.entity.Demo;
import com.modules.sys.service.DemoService;
import com.result.HttpResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.naming.AuthenticationException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CloudBranchApplication.class})
public class CloudBranchApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private DemoService demoService;

    @Test
    //@Transactional(rollbackFor = AuthenticationException.class)
    public void demoTest() throws Exception {
        /*System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
        System.out.println(sortList);*/

        /*BulkMapper<Demo, Object> bm = new BulkMapper<>() {
            @Override
            public Demo mapBulk(List<Object> bulk) {// 实现BulkMapper接口的方法，来把获取到的排序的数据转换为我们需要的返回类型
                Iterator<Object> iterator = bulk.iterator();
                Demo demo = new Demo();
                demo.setCardName((String)iterator.next());
                demo.setCardNumber((long)iterator.next());
                return demo;
            }
        };
        List list = redisTemplate.sort(query, bm);
        System.out.println(list);*/

        /*DefaultTransactionDefinition trans = new DefaultTransactionDefinition();
        trans.setName("mytrans");
        trans.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(trans);
        System.out.println(status);*/

        Demo demo1 = new Demo("testData");
        int num = demoService.insert(demo1);
        System.out.println(num);
        System.out.println(demo1.getId());
        //System.out.println(3/0);
        //transactionManager.commit(status);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }

}

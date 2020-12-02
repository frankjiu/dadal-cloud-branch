/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.modules.sys.controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.modules.sys.controller;

import com.exception.CommonException;
import com.google.common.collect.Lists;
import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import com.modules.sys.model.vo.DemoVo;
import com.modules.sys.service.DemoService;
import com.result.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Description: Demo Controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("findDemoList")
    @SuppressWarnings("rawtypes")
    public HttpResult findDemoList(@RequestBody DemoDto demoDto) throws CommonException {
        List<Demo> demoList;
        try {
            demoList = demoService.findDemoList(demoDto);
        } catch (Exception e) {
            throw new CommonException("数据库查询出错!", e);
        }
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());
        return HttpResult.success(demoVoList);
    }

    @GetMapping("findRedis")
    public HttpResult findDemoList() throws CommonException {
        redisTemplate.opsForValue().set("a", "heiheihei!");
        Object a = redisTemplate.opsForValue().get("a");
        ValueOperations<String, Object> opr = redisTemplate.opsForValue();
        opr.set("demo", new Demo(55, "华夏", 8L, null));
        Demo demo = (Demo)opr.get("demo");
        return HttpResult.success(demo);
    }

    /**
     * 串行测试
     * @return
     */
    @GetMapping("testSerial")
    public HttpResult testThreadNo() {
        long start = System.currentTimeMillis();
        DemoDto demoDto1 = new DemoDto("ABC");
        List<Demo> list1 = demoService.findDemoListByCondition(demoDto1);
        DemoDto demoDto2 = new DemoDto("DEF");
        List<Demo> list2 = demoService.findDemoListByCondition(demoDto2);
        long end = System.currentTimeMillis();
        list1.addAll(list2);
        return HttpResult.success(list1, "耗时:" + (end-start)/1000);
    }

    /**
     * 并行测试
     * @return
     */
    @GetMapping("testParallel")
    public HttpResult testThread() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<DemoDto> conditionList = Lists.newArrayList(new DemoDto("ABC"), new DemoDto("DEF"));
        List<Demo> completeList = new ArrayList<>();
        long start = System.currentTimeMillis();
        CompletableFuture[] futureResult = conditionList.stream().map(object -> CompletableFuture.supplyAsync(() -> demoService.findDemoListByCondition(object), pool)
                .thenApply(k -> k)
                .whenComplete((t, e) -> {
                    System.out.println(">>>>>>任务完成! result=" + t + "; 异常=" + e + "; " + LocalDateTime.now());
                    completeList.addAll(t);
                })).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futureResult).join();
        long end = System.currentTimeMillis();
        return HttpResult.success(completeList, "耗时:" + (end-start)/1000);
    }

    /*public void findPrices() {
        Executor executor = Executors.newCachedThreadPool();
        List<DemoDto> demoDtos = Lists.newArrayList(new DemoDto("ABC"), new DemoDto("DEF"));
        long start = System.currentTimeMillis();
        //Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(),100));
        CompletableFuture<?>[] priceFuture = demoDtos.stream()
                .map(demoDto -> CompletableFuture
                        .supplyAsync(() -> demoService.findDemoListByCondition(demoDto), executor)
                        .thenCombine(CompletableFuture.supplyAsync(() -> demoService.findDemoListByCondition(new DemoDto("ABC")), executor),
                                (quote, rate) -> new Quote(quote.getShop(), quote.getPrice() * rate, quote.getDiscount())))//这返回的是异步处理
                //.map(future->future.thenApply(Quote::parse))//thenApp是前一个对象完成了之后调下个对象的方法（parse）
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> DiscountDemo.applyDiscount(quote), executor)))
                //thenAccept()定义CompletableFuture返回的结果
                .map(future -> future.thenAccept(content -> System.out.println(content + "(done in " + (System.currentTimeMillis() - start) + " msecs")))
                .toArray(size -> new CompletableFuture[size]);
        //allOf接收一个数组，当里面的CompletableFuture都完成的时候，就会执行下一个语句
        CompletableFuture.allOf(priceFuture).thenAccept((obj) -> System.out.println(" all done"));
        //allOf接收一个数组，当里面的CompletableFuture有一个完成时，就会执行下一个语句
        CompletableFuture.anyOf(priceFuture).thenAccept((obj) -> System.out.println("fastest anyOf done " + obj));

    }*/


}

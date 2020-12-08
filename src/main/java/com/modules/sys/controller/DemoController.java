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
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

    @GetMapping("insert")
    public HttpResult insertData() throws Exception {
        Demo demo1 = new Demo("testData");
        int num = demoService.insert(demo1);
        System.out.println(num);
        return HttpResult.success(demo1.getId());
    }

    @PostMapping("findDemoById")
    public HttpResult findDemoById(@RequestParam String id) throws CommonException {
        Demo demo;
        try {
            //demo = ((DemoService)AopContext.currentProxy()).findDemoById(id);
            demo = demoService.findDemoById(id);
        } catch (Exception e) {
            throw new CommonException("数据库查询出错!", e);
        }
        return HttpResult.success(demo);
    }

    @PostMapping("findDemoList")
    public HttpResult findDemoList(@RequestBody DemoDto demoDto) throws CommonException {
        List<Demo> demoList;
        try {
            demoList = demoService.findDemoList();
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

        redisTemplate.opsForValue().set("name","tom",10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().get("name"); //由于设置的是10秒失效，十秒之内查询有结果，十秒之后返回为null

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

}

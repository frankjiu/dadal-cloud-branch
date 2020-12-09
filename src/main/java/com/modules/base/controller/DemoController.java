/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.modules.base.controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.modules.base.controller;

import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.google.common.collect.Lists;
import com.modules.base.model.dto.DemoDto;
import com.modules.base.model.entity.Demo;
import com.modules.base.model.vo.DemoVo;
import com.modules.base.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Slf4j
@Validated
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("demo/page/")
    public HttpResult findPage(@RequestBody @Valid DemoDto demoDto) throws Exception {
        List<Demo> demoList = demoService.findPage(demoDto);
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());
        return HttpResult.success(demoVoList);
    }

    @GetMapping("demo/{id}")
    public HttpResult findById(@PathVariable Integer id) throws Exception {
        Demo demo = demoService.findById(id);
        return HttpResult.success(demo);
    }

    @PostMapping("demo")
    public HttpResult save(@RequestBody @Valid DemoDto demoDto) throws Exception {
        demoDto.setCardName("testData");
        // 数据转换
        Demo demo = new Demo();
        BeanUtils.copyProperties(demoDto, demo);
        Demo savedDemo = demoService.save(demo);
        if (savedDemo.getId() == null) {
            return HttpResult.fail("save failed!");
        }
        return HttpResult.success(savedDemo.getId());
    }

    /**
     * 缓存
     * @return
     * @throws CommonException
     */
    @GetMapping("findRedis")
    public HttpResult findDemoList() {
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

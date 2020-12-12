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
import com.core.result.PageModel;
import com.google.common.collect.Lists;
import com.modules.base.model.dto.DemoGetDto;
import com.modules.base.model.dto.DemoPostDto;
import com.modules.base.model.entity.Demo;
import com.modules.base.model.vo.DemoVo;
import com.modules.base.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
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

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Value("${limited.count}")
    public Integer limitedCount;

    @GetMapping("demo/{id}")
    public HttpResult findById(@PathVariable Integer id) {
        Demo demo = demoService.findById(id);
        if (demo == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(demo);
    }

    /**
     * 查询所有数据, 缓存到redis 30s
     * @return
     */
    @GetMapping("demo")
    public HttpResult findAll() {
        String key = "demo_find_all";
        // 查询Redis
        List<Demo> demoList = (List<Demo>)redisTemplate.opsForValue().get(key);
        // 查询DB
        if (CollectionUtils.isEmpty(demoList)) {
            log.info(">>> Query from db.., the total data limited at: " + limitedCount);
            demoList = demoService.findAll(limitedCount);
            if (demoList.size() > limitedCount) {
                throw new CommonException("Data size overload!");
            }
            redisTemplate.opsForValue().set(key, demoList, 30, TimeUnit.SECONDS); //30s容忍
        }
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());

        return HttpResult.success(demoVoList);
    }

    // @SysLog("findDemoPage")
    // @RequiresPermissions("demo:demo:page")
    @PostMapping("demo/page")
    public HttpResult findPage(@RequestBody @Valid DemoGetDto demoGetDto) {
        List<Demo> demoList = demoService.findPage(demoGetDto);
        int total = demoService.count(demoGetDto);
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());
        PageModel<DemoVo> pageModel = new PageModel<>();
        pageModel.setData(demoVoList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    /**
     * Post or Put
     * @param demoPostDto
     * @return
     */
    @RequestMapping(value = "demo", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid DemoPostDto demoPostDto) {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // data check: has been check with annotation; We can also check in another method with regex and commonException.
        Pattern pattern = Pattern.compile("^[A-Za-z]*$|^[\\u4e00-\\u9fa5]*$");
        boolean match = pattern.matcher(demoPostDto.getCardName()).matches();
        if (!match) {
            throw new CommonException("cardName: required, and only letters or Chinese characters are allowed!");
        }
        // data convert
        Demo demo = new Demo();
        BeanUtils.copyProperties(demoPostDto, demo);
        int num = 0;
        try {
            if (demo.getId() == null) {
                num = demoService.insert(demo);
            } else {
                num = demoService.update(demo);
            }
            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            transactionManager.rollback(transaction);
        }

        if (num <= 0) {
            return HttpResult.fail("save failed!");
        }
        return HttpResult.success(demo.getId());
    }

    @DeleteMapping("demo/{id}")
    public HttpResult delete(@PathVariable Integer id) {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        int num = 0;
        try {
            transactionManager.commit(transaction);
            num = demoService.delete(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            transactionManager.commit(transaction);
        }
        if (num <= 0) {
            return HttpResult.fail("delete failed");
        }
        return HttpResult.success();
    }

    /**
     * 缓存
     */
    @GetMapping("demo/redis/{key}")
    public HttpResult findRedis(@PathVariable String key) {
        Demo data = new Demo(999, "China_Bank_Test", "66668888", new Date());
        redisTemplate.opsForValue().set(key, data, 10, TimeUnit.MINUTES); // 10min effect time
        data = (Demo)redisTemplate.opsForValue().get(key);
        return HttpResult.success(data);
    }

    /**
     * 串行测试
     * @return
     */
    @GetMapping("testSerial")
    public HttpResult testThreadNo() {
        long start = System.currentTimeMillis();
        DemoGetDto demoGetDto1 = new DemoGetDto("ABC");
        List<Demo> list1 = demoService.findPage(demoGetDto1);
        DemoGetDto demoGetDto2 = new DemoGetDto("DEF");
        List<Demo> list2 = demoService.findPage(demoGetDto2);
        long end = System.currentTimeMillis();
        list1.addAll(list2);
        return HttpResult.success(list1, "cost:" + (end-start)/1000);
    }

    /**
     * 并行测试
     * @return
     */
    @GetMapping("testParallel")
    public HttpResult testThread() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<DemoGetDto> conditionList = Lists.newArrayList(new DemoGetDto("ABC"), new DemoGetDto("DEF"));
        List<Demo> completeList = new ArrayList<>();

        long start = System.currentTimeMillis();
        CompletableFuture[] futureResult = conditionList.stream().map(object -> CompletableFuture.supplyAsync(() -> demoService.findPage(object), pool)
                .thenApply(k -> k)
                .whenComplete((t, e) -> {
                    System.out.println(">>>>>>task completed! result=" + t + "; exception=" + e + "; " + LocalDateTime.now());
                    completeList.addAll(t);
                })).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futureResult).join();
        long end = System.currentTimeMillis();
        return HttpResult.success(completeList, "cost:" + (end-start)/1000);
    }

}

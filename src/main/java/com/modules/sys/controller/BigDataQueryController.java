package com.modules.sys.controller;

import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import com.modules.sys.model.vo.DemoVo;
import com.modules.sys.service.DemoService;
import com.result.HttpResult;
import com.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-23
 */
@RestController
@Slf4j
public class BigDataQueryController implements Callable <List<DemoVo>> {

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoDto demoDto;

    private BigDataQueryController (DemoDto demoDto) {
        this.demoDto = demoDto;
    }

    private static final int roundTimes = 18;
    private static final int threadNum = 4;

    /**
     * 串行查询
     * @param demoDto
     * @return
     * @throws Exception
     */
    @PostMapping("findList")
    public HttpResult findList(@RequestBody DemoDto demoDto) throws Exception {
        List<DemoVo> result = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < roundTimes; i++) {
            List<Demo> demoList = demoService.findDemoList(demoDto);
            List<DemoVo> demoVoList = demoList.stream()
                    .map(e -> DemoVo.builder().
                            id(e.getId())
                            .cardName(e.getCardName())
                            .cardNumber(e.getCardNumber())
                            .createTime(e.getCreateTime())
                            .build())
                    .collect(Collectors.toList());

            result.addAll(demoVoList);
        }
        long end = System.currentTimeMillis();
        log.info("=======================串行查询耗时{}s, 结果总量:{}", (end-start)/1000, result.size());
        return HttpResult.success(result);
    }

    @Override
    public List<DemoVo> call() {
        List<DemoVo> demoVoList = null;
        try {
            demoService = SpringContextUtils.getBeanByClass(DemoService.class);
            List<Demo> demoList = demoService.findDemoList(demoDto);
            demoVoList = demoList.stream()
                    .map(e -> DemoVo.builder()
                            .id(e.getId())
                            .cardName(e.getCardName())
                            .cardNumber(e.getCardNumber())
                            .createTime(e.getCreateTime())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return demoVoList;
    }

    /**
     * 并行查询
     * @return
     */
    @PostMapping("findAllList")
    public HttpResult findAllList(@RequestBody DemoDto demoDto){
        List<DemoVo> result = new ArrayList<>();
        List<Future<List<DemoVo>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        long start = System.currentTimeMillis();
        for (int i = 0; i < roundTimes; i++) {
            Future<List<DemoVo>> future = executorService.submit(new BigDataQueryController(demoDto));
            futures.add(future);
        }

        for (Future<List<DemoVo>> future : futures) {
            List<DemoVo> demoVos = null;
            try {
                demoVos = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            result.addAll(demoVos);
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.info("=======================并行查询耗时{}s, 结果总量:{}<<<", (end-start)/1000, result.size());
        return HttpResult.success(result);
    }


}

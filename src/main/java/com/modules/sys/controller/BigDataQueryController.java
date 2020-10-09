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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-23
 */
@RestController
@Slf4j
public class BigDataQueryController implements Runnable {

    @Autowired
    private DemoService demoService;

    private DemoDto demoDto;

    private BigDataQueryController (DemoDto demoDto) {
        this.demoDto = demoDto;
    }

    private static List<DemoDto> list;

    private static List<DemoVo> result;

    static {
        list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            DemoDto d = new DemoDto();
            d.setCardName("中国银行");
            list.add(d);
        }

        /*DemoDto d1 = new DemoDto();
        d1.setCardName("中国银行");
        DemoDto d2 = new DemoDto();
        d2.setCardName("农业银行");
        DemoDto d3 = new DemoDto();
        d3.setCardName("工商银行");
        DemoDto d4 = new DemoDto();
        d3.setCardName("建设银行");
        DemoDto d5 = new DemoDto();
        d3.setCardName("交通银行");

        list.add(d1);
        list.add(d2);
        list.add(d3);
        list.add(d4);
        list.add(d5);*/

    }

    /**
     * 串行查询
     * @param demoDto
     * @return
     * @throws Exception
     */
    @PostMapping("findList")
    public HttpResult findList(@RequestBody DemoDto demoDto) throws Exception {
        result = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            demoDto = list.get(i);
            List<Demo> demoList = demoService.findDemoList(demoDto);

            List<DemoVo> demoVoList = demoList.stream()
                    .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).build())
                    .collect(Collectors.toList());
            result.addAll(demoVoList);
        }
        long end = System.currentTimeMillis();
        log.info("=======================串行查询耗时{}s, 结果总量:{}", (end-start)/1000, result.size());
        return HttpResult.success(result);
    }

    @Override
    public void run() {
        try {
            demoService = SpringContextUtils.getBeanByClass(DemoService.class);
            List<Demo> demoList = demoService.findDemoList(demoDto);

            List<DemoVo> demoVoList = demoList.stream()
                    .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).build())
                    .collect(Collectors.toList());

            result.addAll(demoVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 并行查询
     * @return
     */
    @PostMapping("findAllList")
    public HttpResult findAllList(){
        result = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        log.info(">>>多线程查询开始...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            demoDto = list.get(i);
            executorService.execute(new BigDataQueryController(demoDto));
        }

        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.info("多线程查询结束,耗时{}s, 结果总量:{}<<<", (end-start)/1000, result.size());

        return HttpResult.success(result);

    }





}

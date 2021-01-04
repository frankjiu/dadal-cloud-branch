package com.function.imports;

import com.core.utils.SpringContextUtils;
import com.modules.base.model.entity.Demo;
import com.modules.base.service.DemoService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @Description: 创建 ForkJoinableTask 任务类
 * @Author: QiuQiang
 * @Date: 2020-12-15
 */
@Slf4j
public class ForkJoinableTask extends RecursiveTask<Integer> {

    private int start;
    private int end;
    private List<Demo> list;
    int sum = 0;
    private static final int BATCH_SIZE = 100;

    public ForkJoinableTask(int start, int end, List<Demo> list) {
        this.start = start;
        this.end = end;
        this.list = list;
    }

    @Override
    protected Integer compute() {
        int length = end - start;
        if (length <= BATCH_SIZE) {
            //在BATCH_SIZE内(包括)直接进行处理
            List<Demo> forkList = new ArrayList<>();
            for (int i = start; i < end; i++) {
                list.add(list.get(i));
            }
            try {
                DemoService demoService = SpringContextUtils.getBeanByClass(DemoService.class);
                demoService.batchInsert(forkList);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
            sum++;
            return sum;
        } else {
            //继续拆分
            int middle = (start + end) / 2;
            ForkJoinableTask taskLeft = new ForkJoinableTask(start, middle, list);
            ForkJoinableTask taskRight = new ForkJoinableTask(middle, end, list);
            invokeAll(taskLeft, taskRight);
            int left = taskLeft.join();
            int right = taskRight.join();
            int result = left + right;
            return result;

        }
    }
}
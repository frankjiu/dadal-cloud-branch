package com.function.imports;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-15
 */
@Slf4j
public class ForkJoinHandlerUpgraded extends RecursiveTask {

    private List list;
    private Function function;
    private static final int BATCH_SIZE = 50;

    public ForkJoinHandlerUpgraded(List list, Function function){
        this.list = list;
        this.function = function;
    }

    @Override
    protected Integer compute() {
        if (list.size() <= BATCH_SIZE) {
            //FltBpMinStrategyService fltBpMinStrategyService = SpringContextUtil.getBeanByClass(FltBpMinStrategyService.class);
            //return fltBpMinStrategyService.batchInsert(list);
            return (int) function.apply(list);
        } else {
            //继续拆分
            int middle = list.size() / 2;
            ForkJoinTask taskLeft = new ForkJoinHandlerUpgraded(list.subList(0, middle), function);
            ForkJoinTask taskRight = new ForkJoinHandlerUpgraded(list.subList(middle, list.size()), function);
            invokeAll(taskLeft, taskRight);
            int left = (int) taskLeft.join();
            int right = (int) taskRight.join();
            int result = left + right;
            return result;
        }
    }

}
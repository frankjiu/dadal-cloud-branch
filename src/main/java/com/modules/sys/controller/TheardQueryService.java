
package com.modules.sys.controller;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.*;

/*@Service
public class TheardQueryService {

    SqlHadle sqlHadle=new SqlHadle();

    public List<List> getMaxResult(String table) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();//开始时间
        List<List> result = new ArrayList<>();//返回结果
        //查询数据库总数量
        int count = sqlHadle.count(table);
        int num = 8000;//一次查询多少条
        //需要查询的次数
        int times = count / num;
        if (count % num != 0) {
            times = times + 1;
        }
        //开始页数  连接的是orcle的数据库  封装的分页方式  我的是从1开始
        int bindex = 1;
        //Callable用于产生结果
        List<Callable<List>> tasks = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            Callable<List> qfe = new ThredQuery(bindex, num, table);
            tasks.add(qfe);
            bindex += bindex;
        }
        //定义固定长度的线程池  防止线程过多
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        //Future用于获取结果
        List<Future<List>> futures=executorService.invokeAll(tasks);
        //处理线程返回结果
        if(futures!=null&&futures.size()>0){
            for (Future<List> future:futures){
                result.addAll(future.get());
            }
        }

        executorService.shutdown();//关闭线程池
        long end = System.currentTimeMillis();
        System.out.println("线程查询数据用时:"+(end-start)+"ms");
        return result;
    }


}*/

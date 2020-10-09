package com.modules.sys.controller;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-23
 */


/*
import com.sqlSource.SqlHadle;

import java.util.List;
import java.util.concurrent.Callable;

public class ThredQuery implements Callable<List> {


    SqlHadle sqlHadle=new SqlHadle();

    private String search;//查询条件 根据条件来定义该类的属性

    private int bindex;//当前页数

    private int num;//每页查询多少条

    private String table;//要查询的表名，也可以写死，也可以从前面传

    private List page;//每次分页查出来的数据

    public  ThredQuery(int bindex,int num,String table) {
        this.bindex=bindex;
        this.num=num;
        this.table=table;
        //分页查询数据库数据
        page=sqlHadle.queryTest11(bindex,num,table);
    }

    @Override
    public List call() throws Exception {
        //返回数据给Future
        return page;
    }
}*/

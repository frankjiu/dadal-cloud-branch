package com.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: Used for memory paging
 * @Author: QiuQiang
 * @Date: 2020-09-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {

    private int pageNo;

    private int pageSize;

    private List data;

    public PageBean (int pageSize, List data) {
        this.pageSize = pageSize;
        this.data = data;
    }

}
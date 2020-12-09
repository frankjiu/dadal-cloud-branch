package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String address;
    private Integer age;
    private List<Detail> detailList;

    @Data
    public static class Detail{
        private String description = "年龄信息";
        private Integer age;
    }

}
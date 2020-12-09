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
    private Integer age;
    private List<Detail> details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail{
        private String description;
        private String address;
    }

}
package com.function.es.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-02-05
 */
@Data
public class EsModelVo {

    private String index;
    private String id;
    private String name;
    private Integer age;
    private String hobby;

    @NotNull
    @Min(1)
    private Integer pageNum;
    @NotNull
    @Min(1)
    private Integer pageSize;

}

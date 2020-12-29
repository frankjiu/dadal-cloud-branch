package com.modules.sys.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-29
 */
@Data
public class UserGetDto {

    private String userName;

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    private Integer pageSize;
}

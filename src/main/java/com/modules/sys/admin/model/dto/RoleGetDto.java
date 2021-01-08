package com.modules.sys.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Data
public class RoleGetDto {

    private String roleName;

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    private Integer pageSize;

}

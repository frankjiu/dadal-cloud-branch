package com.modules.sys.admin.model.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Data
public class RolePostDto {

    private Long id;
    private String roleName;
    private String remark;
    private Long updateTime;

}

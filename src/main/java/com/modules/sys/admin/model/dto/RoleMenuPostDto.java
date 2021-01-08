package com.modules.sys.admin.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Data
public class RoleMenuPostDto {

    private Long id;
    private Long roleId;
    private List<Long> menuIds;

}

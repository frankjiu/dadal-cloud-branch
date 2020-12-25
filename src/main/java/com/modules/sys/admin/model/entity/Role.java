package com.modules.sys.admin.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 角色
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Data
public class Role implements Serializable {

    private Long id;                    // ID
    private String roleName;            // 角色名
    private String remark;              // 备注
    private Long updateTime;            // 更新时间

}

package com.modules.sys.admin.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 权限
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Data
public class Perm implements Serializable {

    private Long id;             // ID
    private String name;         // 权限名称
    private String remark;       // 备注
    private Long updateTime;     // 更新时间

}
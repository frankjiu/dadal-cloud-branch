package com.modules.sys.admin.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 权限,作为menu中perm初始化数据(一般同一页面权限相同, 后台可根据需求对具体的query或insert接口进行更细粒度的权控)
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
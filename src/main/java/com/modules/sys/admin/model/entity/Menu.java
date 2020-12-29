package com.modules.sys.admin.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 菜单
 * @Author: QiuQiang
 * @Date: 2020-12-25
 */
@Data
public class Menu implements Serializable {

    private Long id;                    // ID
    private Long parentId;              // 父菜单ID(根菜单为0)
    private Integer type;               // 菜单类型(0:目录 1:菜单 2:按钮)
    private String menuName;            // 菜单名称
    private String url;                 // 菜单URL
    private String perm;                // 授权(多个用逗号分隔, 如 user:list, user:create)
    private Integer orderNum;           // 排序
    private Long updateTime;            // 更新时间

}
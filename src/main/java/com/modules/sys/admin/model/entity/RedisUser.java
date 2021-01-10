package com.modules.sys.admin.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-09
 */
@Data
public class RedisUser implements Serializable {

    private User user;                      // 用户的实体对象
    private Long roleId;                    // 用户的角色ID
    private String token;                   // 用户的token
    private List<Menu> menus;               // 用户的所有菜单
    private List<String> permissions;       // 用户的所有权限

}
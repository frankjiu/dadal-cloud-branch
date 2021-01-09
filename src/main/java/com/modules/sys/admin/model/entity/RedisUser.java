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

    private User user;                      // 这个用户的实体对象
    private Long roleId;                    // 这个用户的角色ID
    private String token;                   // 这个用户的token
    private List<String> permissions;       // 这个用户的所有权限

}
package com.modules.sys.admin.model.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-07
 */
@Data
public class MenuPostDto {

    private Long id;
    private Long parentId;
    private Integer type;
    private String menuName;
    private String url;
    private String perm;
    private Integer orderNum;
    private Long updateTime;

}

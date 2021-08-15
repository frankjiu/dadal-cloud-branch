package com.core.log.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 */
@Data
public class Log implements Serializable {

    private Integer id;                     // 主键
    private String userName;                // 用户名
    private String description;             // 操作描述
    private String url;                     // 请求URL
    private String method;                  // 请求方法
    private String params;                  // 请求参数
    private Long time;                      // 执行时长(毫秒)
    private String ip;                      // IP地址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;                // 创建时间
    private String version;                 // 当前版本

}

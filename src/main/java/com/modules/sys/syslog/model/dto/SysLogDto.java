package com.modules.sys.syslog.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统日志查询条件
 */
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class SysLogDto {

    private String userName;                // 用户名
    private String description;             // 操作描述
    private String method;                  // 请求方法

    private Long time;                      // 执行时长(毫秒)
    private String ip;                      // IP地址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;                // 创建时间

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    private Integer pageSize;

}

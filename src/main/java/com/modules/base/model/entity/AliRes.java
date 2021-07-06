package com.modules.base.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AliRes {

    private String alipay_sdk;
    private String app_id;
    private String biz_content;
    private String version;

}

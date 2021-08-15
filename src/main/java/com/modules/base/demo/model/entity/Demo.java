/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: Demo实体类
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demo {

    private Integer id; //主键
    private String cardName; //卡名
    private String cardNumber; //卡号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    public Demo(String cardName){
        this.cardName = cardName;
    }

    public Demo(String cardName, Date createTime){
        this.cardName = cardName;
        this.createTime = createTime;
    }

}

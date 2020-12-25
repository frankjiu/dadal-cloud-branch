/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: Demp升级
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demp {

    private Integer id; //主键
    private String cardName; //卡名
    private String cardNumber; //卡号
    private Desc desc; //卡号

    @Data
    public static class Desc {
        private String name;
        private Date updateTime;

        public Desc(String name){
            this.name = name;
        }

        public Desc(Date updateTime){
            this.updateTime = updateTime;
        }

    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    public Demp(Desc desc){
        this.desc = desc;
    }

    public Demp(String cardName){
        this.cardName = cardName;
    }

    public Demp(String cardName, Date createTime){
        this.cardName = cardName;
        this.createTime = createTime;
    }

}

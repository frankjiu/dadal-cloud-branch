/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: Demo-查询条件
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class DemoGetDto implements Serializable {

    private Integer id;

    private String cardName;

    private String cardNumber;

    private String createTime;

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    private Integer pageSize;

    public DemoGetDto(String cardName) {
        this.cardName = cardName;
    } //used for test

}

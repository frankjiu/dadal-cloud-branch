/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: xxx参数
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class DemoGetDto {

    private Integer id;

    @NotBlank
    private String cardName;

    @NotBlank
    private String cardNumber; //模糊查询

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull
    @Min(1)
    private Integer pageNum;

    @NotNull
    @Min(1)
    private Integer pageSize;

    public DemoGetDto(String cardName) {
        this.cardName = cardName;
    }

}

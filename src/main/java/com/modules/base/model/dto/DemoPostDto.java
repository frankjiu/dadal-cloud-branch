/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.base.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Description: Demo-POST数据
 * @author: Frankjiu
 * @date: 2020年8月26日
 */
@Data
public class DemoPostDto {

    @org.hibernate.validator.constraints.Range(min = 1, max = 99999999, message = "id: 非必填, 只允许输入1个1~99999999(含)的整数")
    private Integer id;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z]*$|^[\\u4e00-\\u9fa5]*$", message = "cardName: 必填, 仅允许字母或汉字!")
    private String cardName;

    @Pattern(regexp = "^$|^[1-9]\\d*$", message = "cardNumber: not required, only positive integers are allowed!")
    private String cardNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}

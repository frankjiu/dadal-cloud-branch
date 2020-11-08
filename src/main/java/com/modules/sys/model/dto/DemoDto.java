/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月26日
 * @version: V1.0
 */

package com.modules.sys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
public class DemoDto {

    private String id;
    private String cardName;
    private String cardNumber;
    private Date createTime;

}

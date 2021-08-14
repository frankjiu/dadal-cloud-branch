/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年6月3日
 * @version: V1.0
 */

package com.core.anotation;

import java.lang.annotation.*;

/**
 * @Description: 系统日志注解
 * @author: Frankjiu
 * @date: 2020年6月3日
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {

    /**
     * 操作类型
     */
    String type() default "";

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 备注
     */
    String remark() default "";

}
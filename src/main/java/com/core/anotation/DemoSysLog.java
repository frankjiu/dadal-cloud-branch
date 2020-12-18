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
public @interface DemoSysLog {

    /**
     * 操作类型
     */
    String operationType() default "";

    /**
     * 操作模块
     */
    String operationModule() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 备注
     */
    String remark() default "";

}
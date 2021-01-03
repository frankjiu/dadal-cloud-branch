/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月27日
 * @version: V1.0
 */

package com.modules.index;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.exception.CommonException;

/**
 * @Description: TODO
 * @author: Frankjiu
 * @date: 2020年8月27日
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/index")
    public String toIndex() {
        return "here is the index page";
    }

    /**
     * Ajax request(return json)
     */
    @PostMapping("/json")
    public String ajax() throws Exception {
        throw new Exception("ajax请求(参数非法)异常!");
    }

    /**
     * Http request(return page)
     */
    @GetMapping("/page")
    public String page() throws Exception {
        throw new Exception("http请求(数据解析)异常!");
    }

    /**
     * Post request(return json)
     */
    @PostMapping("/test")
    public String test() throws CommonException {
        throw new CommonException("test请求(xx业务处理)异常!");
    }

}

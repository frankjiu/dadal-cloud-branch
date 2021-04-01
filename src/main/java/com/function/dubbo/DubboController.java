package com.function.dubbo;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: Dubbo Consumer
 * @Author: QiuQiang
 * @Date: 2021-04-01
 */
@Controller
public class DubboController {
    //dubbo:service interface="" version="" check=false
    @Reference(interfaceClass = DubboService.class, version = "1.0.0", check = false)
    private DubboService dubboService;

    @RequestMapping(value = "/addStudent")
    public @ResponseBody
    String add(String str) {
        String res = dubboService.addOneData(str);
        return str + res;
    }
}
package com.function.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-21
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/toIndex")
    public String toIndex() {
        return "/index";
    }

}

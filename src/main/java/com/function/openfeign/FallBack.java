package com.function.openfeign;

import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-12
 */
@Component
public class FallBack implements TestCallService {

    @Override
    public Object selectAll() {
        return "服务降级!";
    }

}

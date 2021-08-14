package com.function.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-12
 */
@FeignClient(value = "hyzc-business", url = "http://localhost:8084/", fallback = FallBack.class)
public interface TestCallService {

    @PostMapping("/user/selectAll")
    Object selectAll();

}

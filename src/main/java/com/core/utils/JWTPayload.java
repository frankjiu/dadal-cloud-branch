package com.core.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class JWTPayload {

    private Long id;
    private Integer accountType;

    public static final int ADMIN_TYPE = 0;
    public static final int MANAGER_TYPE = 1;
    public static final int STAFF_TYPE = 3;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}

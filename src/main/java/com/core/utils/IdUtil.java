package com.core.utils;

import com.wlhlwl.hyzc.customer.core.constant.Constant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @Description: id = 时间前缀 + 自增后缀
 * @Author: QiuQiang
 * @Date: 2021-05-25
 */
@Component
public class IdUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static String currentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return LocalDateTime.now(ZoneOffset.ofHours(8)).format(formatter);
    }

    public Long generatId() {
        // 从redis获取自增值
        Object val = redisTemplate.opsForValue().get(Constant.INCR_ID);
        if (val != null && Integer.valueOf(val.toString()) < 9999999) {
            // 自增
            val = redisTemplate.opsForValue().increment(Constant.INCR_ID);
        } else {
            // 如果为空, 重新从0开始, 设置有效期为60s
            val = 0L;
            redisTemplate.opsForValue().set(Constant.INCR_ID, val, Constant.INCR_ID_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return Long.valueOf(currentDateTime() + String.format("%1$07d", val));
    }

}






package com;

import com.core.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CloudBranchApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void demoTest() throws Exception {
        String url = "https://autumnfish.cn/search?keywords=热爱毛主席";
        String jsonData = restTemplate.getForObject(url, String.class);// 包含 access_token 等信息
        if (StringUtils.contains(jsonData, "errcode")) {
            HttpResult.fail("wx login failed!"); //failed
        }
        // 数据加密
        String encodeData = DigestUtils.md5Hex(jsonData + "HYZC_WX_LOGIN");
        // 构建redisKey, 存入redis
        String redisKey = "WX_LOGIN_" + encodeData;
        redisTemplate.opsForValue().set(redisKey, jsonData, Duration.ofDays(3));
        // 构建 ticketKey 返回前端
        String ticketKey = "HYZC_" + encodeData;
        JSONObject js = new JSONObject();
        js.put("wx_token", ticketKey);
        HttpResult rs = HttpResult.success(js);
        System.out.println(rs); // HttpResult(success=true, code=11111, message=Success, data={"ticket":"HYZC06e007f0387714ed0304b37d285416be"})
    }

}

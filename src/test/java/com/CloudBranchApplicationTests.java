package com;

import com.modules.base.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CloudBranchApplication.class})
@Slf4j
public class CloudBranchApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DemoService demoService;

    @Test
    public void demoTest() throws Exception {

    }

}

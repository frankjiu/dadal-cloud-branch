package com.function.redis;

import com.core.utils.RedisUtil;
import net.sf.saxon.expr.flwor.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @Description: 消费消息: 消费端轮询队列delayqueue，将元素排序后取最小时间与当前时间比对，如小于当前时间代表已经过期移除key。
 * @Author: QiuQiang
 * @Date: 2021-08-19
 */
public class TestRedis {

    @Autowired
    private Jedis jedis;

    /*public void pollOrderQueue() throws InterruptedException {
        while (true) {
            Set<Tuple> set = jedis.zrangeWithScores(DELAY_QUEUE, 0, 0);
            String value = ((Tuple) set.toArray()[0]).getElement();
            int score = (int) ((Tuple) set.toArray()[0]).getScore();

            Calendar cal = Calendar.getInstance();
            int nowSecond = (int) (cal.getTimeInMillis() / 1000);
            if (nowSecond >= score) {
                jedis.zrem(DELAY_QUEUE, value);
                System.out.println(sdf.format(new Date()) + " removed key:" + value);
            }

            if (jedis.zcard(DELAY_QUEUE) <= 0) {
                System.out.println(sdf.format(new Date()) + " zset empty ");
                return;
            }
            Thread.sleep(1000);
        }
    }*/

}

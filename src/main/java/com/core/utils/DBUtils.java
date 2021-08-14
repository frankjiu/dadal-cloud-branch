package com.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DBUtils {
    /**
     * 得到系统当前时间(毫秒数)
     * @return
     */
    public static long getSystemTime() {
        return System.currentTimeMillis();
    }

    /**
     * 得当系统当前时间(秒数)
     * @return
     */
    public static long getSystemTimeSecond() {
        Long current = System.currentTimeMillis();
        String curStr = current.toString();
        String str = curStr.substring(0, curStr.length()-3);
        return Long.parseLong(str);
    }

    public static String getMD5Pwd(String origin) {

        //TODO 密码MD5
        return origin;
    }

    @Value("${snowflake.workerid}")
    private long cfgWokerId;
    @Value("${snowflake.datacenterId}")
    private long cfgDatacenterId;

    @PostConstruct
    public void init() {
        workerId = cfgWokerId;
        datacenterId = cfgDatacenterId;
    }

    private static long workerId; // 这个就是代表了机器id
    private static long datacenterId; // 这个就是代表了机房id
    private static long sequence; // 这个就是代表了一毫秒内生成的多个id的最新序号
    private static long twepoch = 1288834974657L;
    private static long workerIdBits = 5L;
    private static long datacenterIdBits = 5L;

    // 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
    private static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 这个是一个意思，就是5 bit最多只能有31个数字，机房id最多只能是32以内
    private static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private static long sequenceBits = 12L;
    private static long workerIdShift = sequenceBits;
    private static long datacenterIdShift = sequenceBits + workerIdBits;
    private static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private static long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static long lastTimestamp = -1L;
    public static long getWorkerId(){
        return workerId;
    }
    public static long getDatacenterId() {
        return datacenterId;
    }

    /**
     * 雪花算法生成ID
     * @return
     */
    // 这个是核心方法，通过调用nextId()方法，让当前这台机器上的snowflake算法程序生成一个全局唯一的id
    public static synchronized long nextId() {
        // 这儿就是获取当前时间戳，单位是毫秒
        long timestamp = getSystemTime();
        if (timestamp < lastTimestamp) {
            System.err.printf(
                    "clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        // 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
        // 这个时候就得把seqence序号给递增1，最多就是4096
        if (lastTimestamp == timestamp) {

            // 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
            //这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }

        } else {
            sequence = 0;
        }
        // 这儿记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;
        // 这儿就是最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) | sequence;
    }

    private static long tilNextMillis(long lastTimestamp) {

        long timestamp = getSystemTime();

        while (timestamp <= lastTimestamp) {
            timestamp = getSystemTime();
        }
        return timestamp;
    }
}

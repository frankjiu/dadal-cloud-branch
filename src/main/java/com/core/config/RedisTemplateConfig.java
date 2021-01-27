package com.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: RedisTemplate config
 * @Author: QiuQiang
 * @Date: 2020-11-15
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * redisTemplate 配置
     * @param factory
     * @return
     */
    @Bean(name = "redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate<String, Object>对象
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);
        // 定义Jackson2JsonRedisSerializer序列化对象
        Jackson2JsonRedisSerializer<T> jacksonSerializer = new Jackson2JsonRedisSerializer<>((Class<T>) Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域(GETTER,SETTER,FIELD,ALL) 以及修饰符范围(PUBLIC_ONLY, NON_PRIVATE, ANY)
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型(类必须是非final修饰的, final修饰的类如String, Integer等会报异常)
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jacksonSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer); // redis key 序列化方式使用stringSerial
        redisTemplate.setValueSerializer(jacksonSerializer); // redis value 序列化方式使用jackson
        redisTemplate.setHashKeySerializer(stringSerializer); // redis hash key 序列化方式使用stringSerial
        redisTemplate.setHashValueSerializer(jacksonSerializer); // redis hash value 序列化方式使用jackson
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
package com.core.config;

import com.core.constant.Constant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @Description: 自定义cacheManager缓存管理器配置(在配置文件中替换springboot默认缓存管理器simple为redis)
 * @Author: QiuQiang
 * @Date: 2020-12-07
 */
@Configuration
@EnableCaching
public class CacheManagerConf {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 默认缓存30分钟
     */
    @Bean("redisCacheManager")
    @Primary
    public RedisCacheManager redisCacheManager() {
        return buildRedisCacheManager(Duration.ofSeconds(30*60));
    }

    /**
     * Demo业务缓存1分钟
     */
    @Bean("demoCacheManager")
    public RedisCacheManager demoCacheManager() {
        return buildRedisCacheManager(Duration.ofSeconds(60));
    }

    /**
     * 创建 RedisCacheManager
     */
    private RedisCacheManager buildRedisCacheManager(Duration duration) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(objectMapper);
        Assert.notNull(serializer, "RedisSerializer can't be null!");
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .computePrefixWith(x -> Constant.APPLICATION_NAME + "_CACHE" + "::" + x + "::")
                .entryTtl(duration);
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder.cacheDefaults(configuration);
        return builder.build();
    }


}

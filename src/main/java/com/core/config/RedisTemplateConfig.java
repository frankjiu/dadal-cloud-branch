package com.core.config;

import com.core.config.serializer.GzipRedisSerializer;
import com.core.config.serializer.Lz4RedisSerializer;
import com.core.constant.Constant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import jodd.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Redis配置
 */
//@Configuration
//@EnableCaching
public class RedisTemplateConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean("gzipRedisTemplate")
    public <V> RedisTemplate<String, V> gzipRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        setSerializer(template, new GzipRedisSerializer(getJacksonSerializer()));
        template.afterPropertiesSet();
        return template;
    }

    @Bean("lz4RedisTemplate")
    public <V> RedisTemplate<String, V> lz4RedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        setSerializer(template, new Lz4RedisSerializer(getJacksonSerializer()));
        template.afterPropertiesSet();
        return template;
    }


    @Bean("redisTemplate")
    @Primary
    public <V> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * upload模块使用的redisTemplate,将使用关闭defaultTyping的jackson序列化器
     */
    @Bean("redisTemplateUpload")
    public <V> RedisTemplate<String, V> redisTemplateUpload(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        setSerializer(template, false);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("springRedisCacheManager")
    @Primary
    public RedisCacheManager redisCacheManager() {
        //默认是缓存12小时
        return buildRedisCacheManager(Duration.ofHours(12));
    }


    @Bean
    public RedissonClient redisson(RedisProperties redisProperties) {

        Config config = new Config();
        if (redisProperties.getCluster() != null && redisProperties.getCluster().getNodes().size() != 0) {
            ClusterServersConfig servers = config.useClusterServers();
            List<String> nodes = redisProperties.getCluster().getNodes();
            if (StringUtil.isNotBlank(redisProperties.getPassword())) {
                servers.setPassword(redisProperties.getPassword());
            }
            nodes.forEach(x -> servers.addNodeAddress("redis://" + x));

        } else {
            SingleServerConfig server = config.useSingleServer();
            server.setDatabase(redisProperties.getDatabase());
            if (StringUtil.isNotBlank(redisProperties.getPassword())) {
                server.setPassword(redisProperties.getPassword());
            }
            server.setDatabase(redisProperties.getDatabase());
            server.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        }

        return Redisson.create(config);
    }


    private void setSerializer(RedisTemplate template, RedisSerializer redisSerializer) {
        //key的序列化器为string
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //value的序列化为json格式,使用Jackson2JsonRedisSerializer
        template.setDefaultSerializer(redisSerializer);//先 全部为json序列化
        template.setKeySerializer(stringRedisSerializer);//key为String序列化
        template.setHashKeySerializer(stringRedisSerializer);//hashKey为String序列化
    }

    private void setSerializer(RedisTemplate template) {
        setSerializer(template, getJacksonSerializer());
    }

    private void setSerializer(RedisTemplate template, boolean isDefaultTyping) {
        setSerializer(template, getJacksonSerializer(isDefaultTyping));
    }

    @Autowired
    public RedisTemplateConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 构建一个RedisCacheManager
     */
    private <T> RedisCacheManager buildRedisCacheManager(Duration duration) {
        Jackson2JsonRedisSerializer<Object> serializer = getJacksonSerializer();
        Assert.notNull(serializer, "RedisSerializer can not be null!");
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .computePrefixWith(x -> Constant.APPLICATION_NAME + "::" + x + "::")
                .entryTtl(duration);
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder.cacheDefaults(configuration);
        return builder.build();
    }

    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer() {
        return getJacksonSerializer(true);
    }

    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer(boolean isDefaultTyping) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        if (isDefaultTyping) {
            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

}

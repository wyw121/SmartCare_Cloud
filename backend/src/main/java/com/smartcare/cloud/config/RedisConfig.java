package com.smartcare.cloud.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 
 * 功能:
 * 1. 启用Spring Cache缓存
 * 2. 配置RedisTemplate序列化
 * 3. 配置分级缓存策略（不同数据类型不同TTL）
 * 
 * @author SmartCare Team
 * @date 2024-01-20
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 使用Jackson2序列化,支持对象存储
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置分级CacheManager
     * 根据数据特性设置不同的缓存过期时间
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 默认配置：30分钟过期
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues(); // 不缓存空值，防止缓存穿透

        // 为不同的cache name设置不同的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // === 基础数据：1小时（变化很少） ===
        cacheConfigurations.put("elderly", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("doctor", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("user", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("family", defaultConfig.entryTtl(Duration.ofHours(1)));

        // === 统计数据：10分钟（需要较高实时性） ===
        cacheConfigurations.put("statistics", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("dashboard", defaultConfig.entryTtl(Duration.ofMinutes(10)));

        // === 健康记录：30分钟（中等实时性） ===
        cacheConfigurations.put("healthRecord", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("assessment", defaultConfig.entryTtl(Duration.ofMinutes(30)));

        // === 配置数据：24小时（几乎不变） ===
        cacheConfigurations.put("config", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put("dict", defaultConfig.entryTtl(Duration.ofHours(24)));

        // === 预警数据：5分钟（高实时性） ===
        cacheConfigurations.put("healthWarning", defaultConfig.entryTtl(Duration.ofMinutes(5)));

        // === 报表数据：15分钟 ===
        cacheConfigurations.put("report", defaultConfig.entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware() // 事务感知
                .build();
    }
}

package com.github.example.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.springboot.cache.RedisCacheKeyTtlHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangChen
 * @since 2021-01-19 11:36
 **/
@Configuration
@EnableCaching
public class RedisCacheManagerCustomConfig {

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();


    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory, RedisSerializer<?> redisSerializer) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
                                     RedisSerializer<?> redisSerializer) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(getRedisCacheConfigurationWithTtl(30L, redisSerializer))
                .withInitialCacheConfigurations(getRedisCacheConfigurationMap(redisSerializer))
                .build();
    }


    public static Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(RedisSerializer<?> redisSerializer) {
        // find all cache key ttl
        Map<String, Long> allKeyTtl = RedisCacheKeyTtlHolder.getAllKeyTtl();

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(allKeyTtl.values().size());
        allKeyTtl.forEach((cacheName, ttl) -> redisCacheConfigurationMap.put(cacheName,
                getRedisCacheConfigurationWithTtl(ttl, redisSerializer)));
        return redisCacheConfigurationMap;
    }

    public static RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Long seconds, RedisSerializer<?> redisSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)).entryTtl(Duration.ofSeconds(seconds));

    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;

    }

}

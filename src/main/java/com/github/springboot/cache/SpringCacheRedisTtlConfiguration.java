package com.github.springboot.cache;

import com.github.springboot.cache.properties.SpringCacheRedisTtlProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangChen
 * @since 2021-12-24 14:54
 **/
public class SpringCacheRedisTtlConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
                                     SpringCacheRedisTtlProperties ttlProperties,
                                     RedisSerializer<?> redisSerializer) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(getRedisCacheConfigurationWithTtl(ttlProperties.getGlobal().getSeconds(), redisSerializer))
                .withInitialCacheConfigurations(getRedisCacheConfigurationMap(redisSerializer))
                .build();
    }


    public static Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(RedisSerializer<?> redisSerializer) {
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
}

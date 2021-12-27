package com.github.springboot.cache.properties;

import com.github.springboot.cache.RedisCacheKeyTtlHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Map;

/**
 * @author WangChen
 * @since 2021-12-24 16:08
 **/
@ConfigurationProperties("spring.redis.ttl")
public class SpringCacheRedisTtlProperties implements InitializingBean {

    public static final Duration DEFAULT = Duration.ofDays(1);

    /**
     * key global ttl
     */
    private Duration global;

    /**
     * cache name ttl config
     */
    private CacheConfig config;

    public SpringCacheRedisTtlProperties() {
        this.global = DEFAULT;
    }

    public Duration getGlobal() {
        return global;
    }

    public void setGlobal(Duration global) {
        this.global = global;
    }

    public CacheConfig getConfig() {
        return config;
    }

    public void setConfig(CacheConfig config) {
        this.config = config;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringCacheRedisTtlProperties.CacheConfig config = getConfig();
        if (!CollectionUtils.isEmpty(config.getCache())){
            config.getCache().forEach(RedisCacheKeyTtlHolder::addCache);
        }
    }

    public static class CacheConfig {

        private boolean override;
        private Map<String, String> cache;

        public boolean isOverride() {
            return override;
        }

        public void setOverride(boolean override) {
            this.override = override;
        }

        public Map<String, String> getCache() {
            return cache;
        }

        public void setCache(Map<String, String> cache) {
            this.cache = cache;
        }
    }
}

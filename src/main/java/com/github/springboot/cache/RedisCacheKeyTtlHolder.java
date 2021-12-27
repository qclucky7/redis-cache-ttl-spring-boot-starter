package com.github.springboot.cache;

import com.github.springboot.cache.util.PeriodFormatterUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WangChen
 * @since 2021-12-24 17:44
 **/
public class RedisCacheKeyTtlHolder implements ReflectionUtils.MethodCallback {

    private static final Map<String, Long> REDIS_KET_TTL = new ConcurrentHashMap<>();
    private Class<?> beanClass;
    private boolean override;

    public RedisCacheKeyTtlHolder(Class<?> beanClass, boolean override) {
        this.beanClass = beanClass;
        this.override = override;
    }

    @Override
    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
        String periodString = method.getAnnotation(Ttl.class).value();
        long ttl;
        try {
            ttl = PeriodFormatterUtil.covertToSecond(periodString);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Cache @Ttl unable to parse period string in %s, please check the writing style",
                    method.getDeclaringClass().getName() + "." + method.getName()));
        }
        CacheConfig cacheConfig = beanClass.getAnnotation(CacheConfig.class);
        if (!ObjectUtils.isEmpty(cacheConfig)) {
            if (override) {
                REDIS_KET_TTL.put(cacheConfig.cacheNames()[0], ttl);
            } else {
                REDIS_KET_TTL.putIfAbsent(cacheConfig.cacheNames()[0], ttl);
            }
            return;
        }
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        if (!ObjectUtils.isEmpty(cacheable)) {
            if (override) {
                REDIS_KET_TTL.put(cacheable.cacheNames()[0], ttl);
            } else {
                REDIS_KET_TTL.putIfAbsent(cacheable.cacheNames()[0], ttl);
            }
        }
        CachePut cachePut = method.getAnnotation(CachePut.class);
        if (!ObjectUtils.isEmpty(cachePut)) {
            if (override) {
                REDIS_KET_TTL.put(cachePut.cacheNames()[0], ttl);
            } else {
                REDIS_KET_TTL.putIfAbsent(cachePut.cacheNames()[0], ttl);
            }
        }
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
        if (!ObjectUtils.isEmpty(cacheEvict)) {
            if (override) {
                REDIS_KET_TTL.put(cacheEvict.cacheNames()[0], ttl);
            } else {
                REDIS_KET_TTL.putIfAbsent(cacheEvict.cacheNames()[0], ttl);
            }
        }
    }

    public static Map<String, Long> getAllKeyTtl() {
        return REDIS_KET_TTL;
    }


    /**
     * Read the add from the configuration file
     * @param cacheName cacheName
     * @param periodString periodString
     */
    public static void addCache(String cacheName, String periodString){
        long ttl;
        try {
            ttl = PeriodFormatterUtil.covertToSecond(periodString);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("the configuration file cannot be parse period string cacheName %s period %s, please check the writing style",
                    cacheName, periodString));
        }
        REDIS_KET_TTL.put(cacheName, ttl);
    }
}

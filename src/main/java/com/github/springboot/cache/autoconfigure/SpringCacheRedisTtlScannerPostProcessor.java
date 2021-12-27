package com.github.springboot.cache.autoconfigure;

import com.github.springboot.cache.RedisCacheKeyTtlHolder;
import com.github.springboot.cache.Ttl;
import com.github.springboot.cache.properties.SpringCacheRedisTtlProperties;
import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangChen
 * @since 2021-12-24 16:56
 **/
public class SpringCacheRedisTtlScannerPostProcessor extends InstantiationAwareBeanAfterPostProcessorAdapter {

    private static final List<Class<? extends Annotation>> CACHE_OPERATION_ANNOTATIONS = new ArrayList<>(4);
    private SpringCacheRedisTtlProperties springCacheRedisTtlProperties;

    public SpringCacheRedisTtlScannerPostProcessor(SpringCacheRedisTtlProperties springCacheRedisTtlProperties) {
        this.springCacheRedisTtlProperties = springCacheRedisTtlProperties;
    }

    private ReflectionUtils.MethodFilter ttlFilter = method -> {
        Set<Class<? extends Annotation>> methodAnn = Stream.of(method.getAnnotations())
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());
        return methodAnn.contains(Ttl.class) && CollectionUtils.containsAny(methodAnn, CACHE_OPERATION_ANNOTATIONS);
    };

    @Override
    public void doPostProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        ReflectionUtils.doWithMethods(beanClass, new RedisCacheKeyTtlHolder(beanClass,
                springCacheRedisTtlProperties.getConfig().isOverride()), ttlFilter);
    }

    static {
        CACHE_OPERATION_ANNOTATIONS.add(Cacheable.class);
        CACHE_OPERATION_ANNOTATIONS.add(CacheEvict.class);
        CACHE_OPERATION_ANNOTATIONS.add(CachePut.class);
        CACHE_OPERATION_ANNOTATIONS.add(Caching.class);
    }

}

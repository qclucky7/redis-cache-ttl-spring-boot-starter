package com.github.springboot.cache.autoconfigure;

import com.github.springboot.cache.SpringCacheRedisTtlConfiguration;
import com.github.springboot.cache.properties.SpringCacheRedisTtlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author WangChen
 * @since 2021-12-24 16:10
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@EnableConfigurationProperties(SpringCacheRedisTtlProperties.class)
@Import({SpringCacheRedisTtlScannerPostProcessor.class, SpringCacheRedisTtlConfiguration.class})
public @interface EnableSpringCacheRedisTtl {

}

package com.github.springboot.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author WangChen
 * @since 2021-12-24 14:56
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Ttl {

    /**
     * Cache key expiration time
     *
     * @see com.github.springboot.cache.util.PeriodFormatterUtil
     * @return long
     */
    @AliasFor("period")
    String value() default "1d";


    @AliasFor("value")
    String period() default "1d";
}

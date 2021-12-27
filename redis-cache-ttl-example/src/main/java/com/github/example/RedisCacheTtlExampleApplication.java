package com.github.example;

import com.github.springboot.cache.autoconfigure.EnableSpringCacheRedisTtl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSpringCacheRedisTtl
@SpringBootApplication
public class RedisCacheTtlExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheTtlExampleApplication.class, args);
    }

}

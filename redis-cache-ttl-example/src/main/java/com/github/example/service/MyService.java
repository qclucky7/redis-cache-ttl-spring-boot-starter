package com.github.example.service;

import com.github.springboot.cache.Ttl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author WangChen
 * @since 2021-12-25 14:01
 **/
@Service
public class MyService {

    @Ttl("1h")
    @Cacheable(cacheNames = "user", key = "#id")
    public User getUser(String id){
        return new User("user");
    }

    @Ttl("100s")
    @Cacheable(cacheNames = "org", key = "#id")
    public Org getOrg(String id){
        return new Org("org");
    }
}

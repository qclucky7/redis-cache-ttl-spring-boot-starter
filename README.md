# redis-cache-ttl-spring-boot-starter

dynamically configure redis cache ttl

[中文文档](https://github.com/GravityMatrix/redis-cache-ttl-spring-boot-starter/blob/main/README-CN.md)

[example](https://github.com/GravityMatrix/redis-cache-ttl-spring-boot-starter/tree/main/redis-cache-ttl-example/src/main/java/com/github/example)

### 1. Pom file
```
<dependency>
    <groupId>com.github</groupId>
    <artifactId>redis-cache-ttl-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Add annotations to the startup class
```java
@EnableSpringCacheRedisTtl
```

### 3. Config
There are two ways to configure cache keys, 
Configuration files take precedence over annotations,
if specify overrides as true in the configuration file, the value of the annotation is used

##### 1. Write to the configuration file

```yaml
spring:
  redis:
    ttl:
      //The default validity period of all keys
      global: 60s
      config:
        // default false, if ture the value of the annotation overrides the current value
        override: ture
        //Cache Key Configuration
        cache:
          user: 30s
          org: 30s

```

##### 2. Using annotations

```java
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
```

### 4. How to use period
[Joda-time](https://github.com/JodaOrg/joda-time) is used as the parsing library

d = day  
h = hours  
m = minutes  
s = seconds  

Use in configuration files or annotations, 1day or 1hours custom config



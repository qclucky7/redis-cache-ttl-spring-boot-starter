# redis-cache-ttl-spring-boot-starter

springboot动态配置redis缓存key过期时间

[案例](https://github.com/GravityMatrix/redis-cache-ttl-spring-boot-starter/tree/main/redis-cache-ttl-example/src/main/java/com/github/example)

### 1. 在pom文件引入
```
<dependency>
    <groupId>com.github</groupId>
    <artifactId>redis-cache-ttl-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 在启动类添加注解
```java
@EnableSpringCacheRedisTtl
```

### 3. 配置缓存名和过期时间
配置缓存键有两种方式，配置文件优先于注释。  
如果在配置文件中指定override为true，则使用注释的值

##### 1. 在配置文件配置

```yaml
spring:
  redis:
    ttl:
      //全局默认缓存过期时间
      global: 60s
      config:
        // 如果为ture的话, 注解缓存过期值会覆盖配置文件的值
        override: ture
        //不同缓存名和过期时间配置
        cache:
          user: 30s
          org: 30s

```

##### 2. 使用注解

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

### 4. 时间区间规范
[Joda-time](https://github.com/JodaOrg/joda-time) 为解析库

d = 天  
h = 小时  
m = 分钟  
s = 秒  

在配置文件或者注解中使用, 比如1d, 1h。



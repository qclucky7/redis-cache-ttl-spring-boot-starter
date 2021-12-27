package com.github.example;

import com.github.example.service.MyService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedisCacheTtlExampleApplicationTests {

    @Autowired
    private MyService myService;

    @Test
    void contextLoads() {
        myService.getUser("123");
        myService.getOrg("123");
    }
}

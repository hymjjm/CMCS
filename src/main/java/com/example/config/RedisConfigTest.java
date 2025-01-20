package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisConfigTest {

    @Value("${spring.redis.port}")
    private int redisPort;

    @PostConstruct
    public void test() {
        System.out.println("Redis Port from System Env: " + redisPort);
    }
}

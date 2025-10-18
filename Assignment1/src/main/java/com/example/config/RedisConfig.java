package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.user}")
    private String redisUser;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public UnifiedJedis jedis() {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user(redisUser)
                .password(redisPassword)
                .build();

        return new UnifiedJedis(
            new HostAndPort(redisHost, redisPort),
            config
        );
    }
}
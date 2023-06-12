package com.jason.common.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Redisson配置类
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
public class RedissonConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private String port;
    @Value("${spring.data.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setConnectionPoolSize(50)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(3000)
                .setTimeout(3000)
                .setDatabase(5);
        return Redisson.create(config);
    }
}

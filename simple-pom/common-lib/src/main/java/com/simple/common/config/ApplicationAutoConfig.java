package com.simple.common.config;

import com.simple.common.redis.MessageQueueProxy;
import com.simple.common.redis.RedisMessageQueueClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class ApplicationAutoConfig {
    @Bean
    public MessageQueueProxy messageQueueProxy(RedisMessageQueueClient MessageQueueProvider) {
        return new MessageQueueProxy(MessageQueueProvider);
    }
}

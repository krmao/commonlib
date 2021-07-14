package com.simple.common.config;

import com.simple.common.redis.MessageQueueProxy;
import com.simple.common.redis.RedisMessageQueueClient;
import com.simple.common.redis.SimpleRedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.simple.common.aop.SentryClientAspect;

/**
 * Use this common config for Web App
 */
@Configuration
@Import(value = {ApplicationAutoConfig.class,SimpleBaseConfig.class, SentryClientAspect.class, SimpleRedisClient.class})
public class SimpleWebConfig {

}

package com.simple.common.config;

import com.simple.common.aop.SentryClientAspect;
import com.simple.core.redis.RedisClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.simple.common.error.GlobalExceptionTranslator;

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = {SimpleBaseConfig.class, SentryClientAspect.class, GlobalExceptionTranslator.class})
public class SimpleRestConfig {
}

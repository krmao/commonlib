package com.simple.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.simple.common.aop.SentryClientAspect;

/**
 * Use this common config for Web App
 */
@Configuration
@Import(value = {SimpleBaseConfig.class, SentryClientAspect.class,})
public class SimpleWebConfig {
}

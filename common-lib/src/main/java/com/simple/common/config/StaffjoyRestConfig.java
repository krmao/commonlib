package com.simple.common.config;

import com.simple.common.aop.SentryClientAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.simple.common.error.GlobalExceptionTranslator;
import com.simple.common.aop.*;

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = {StaffjoyConfig.class, SentryClientAspect.class, GlobalExceptionTranslator.class})
public class StaffjoyRestConfig  {
}

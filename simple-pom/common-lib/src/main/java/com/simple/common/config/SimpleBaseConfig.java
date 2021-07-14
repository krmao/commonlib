package com.simple.common.config;

import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import com.simple.common.auth.AuthenticationInterceptor;
import com.simple.common.auth.AuthorizeInterceptor;
import com.simple.common.auth.FeignRequestHeaderInterceptor;
import com.simple.common.auth.SessionUserResolver;
import com.simple.common.redis.MessageQueueProvider;
import com.simple.common.redis.MessageQueueProxy;
import com.simple.common.redis.RedisMessageQueueClient;
import feign.RequestInterceptor;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.simple.common.env.EnvConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SimpleProps.class)
public class SimpleBaseConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active:NA}")
    private String activeProfile;

    @Value("${spring.application.name:NA}")
    private String appName;

    @Autowired
    SimpleProps simpleProps;


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EnvConfig envConfig() {
        return EnvConfig.getEnvConfg(activeProfile);
    }

    @Bean
    public SentryClient sentryClient() {

        SentryClient sentryClient = Sentry.init(simpleProps.getSentryDsn());
        sentryClient.setEnvironment(activeProfile);
        sentryClient.setRelease(simpleProps.getDeployEnv());
        sentryClient.addTag("service", appName);

        return sentryClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor());
        registry.addInterceptor(new AuthorizeInterceptor());

    }



    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestHeaderInterceptor();
    }

    //添加自定义的拦截器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SessionUserResolver());
    }

    @PostConstruct
    public void init() {
        // init structured logging
        StructLog4J.setFormatter(JsonFormatter.getInstance());

        // global log fields setting
        StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
                "env", activeProfile,
                "service", appName});
    }

    @PreDestroy
    public void destroy() {
        sentryClient().closeConnection();
    }
}

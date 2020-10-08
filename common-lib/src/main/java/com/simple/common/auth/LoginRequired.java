package com.simple.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录验证注解
 * 可用于方法上并且是运行时有效
 * @author hejinguo
 * @version $Id: HandlerAuthLoginContextInterceptor.java, v 0.1 2019年11月17日 下午5:37:11
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}

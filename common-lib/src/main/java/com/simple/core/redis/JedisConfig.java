package com.simple.core.redis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JedisConfig {
    /**redis连接IP地址*/
    public static String  redis_hostName;
    /**redis连接密码*/
    public static String  redis_passWord;
    /**redis连接端口*/
    public static Integer redis_hostPort;
    /**redis连接池最大连接数*/
    public static Integer redis_maxActive;
    /**redis连接池最大空闲连接数*/
    public static Integer redis_maxIdle;
    /**连接池资源耗尽最大连接等待时间*/
    public static Integer redis_maxWait;
    /**检测获取资源是有有效*/
    public static Boolean redis_testOnBorrow;

    @Value("${redis.hostName:localhost}")
    public void setRedis_hostName(String redis_hostName) {
        JedisConfig.redis_hostName = redis_hostName;
    }

    @Value("${redis.redisport:6379}")
    public void setRedis_hostPort(Integer redis_hostPort) {
        JedisConfig.redis_hostPort = redis_hostPort;
    }

    @Value("${redis.maxActive:500}")
    public void setRedis_maxActive(Integer redis_maxActive) {
        JedisConfig.redis_maxActive = redis_maxActive;
    }

    @Value("${redis.maxIdle:100}")
    public void setRedis_maxIdle(Integer redis_maxIdle) {
        JedisConfig.redis_maxIdle = redis_maxIdle;
    }

    @Value("${redis.maxWait:1000}")
    public void setRedis_maxWait(Integer redis_maxWait) {
        JedisConfig.redis_maxWait = redis_maxWait;
    }

    @Value("${redis.testOnBorrow:true}")
    public void setRedis_testOnBorrow(Boolean redis_testOnBorrow) {
        JedisConfig.redis_testOnBorrow = redis_testOnBorrow;
    }

    @Value("${redis.passWord:meetinglive}")
    public  void setRedis_passWord(String redis_passWord) {
        JedisConfig.redis_passWord = redis_passWord;
    }
    

}

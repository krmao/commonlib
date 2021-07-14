package com.simple.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisCacheClient {
    private final RedisTemplate<String, Object> redisTemplate;
    public RedisTemplate<String, Object> getTemplate(){
        return this.redisTemplate;
    }
    public RedisTemplate<String, Object> template(){
        return this.redisTemplate;
    }
    public ValueOperations<String, Object> getOperations(){
        return this.redisTemplate.opsForValue();
    }
    public ValueOperations<String, Object> operations(){
        return this.redisTemplate.opsForValue();
    }
    public void set(String key, Object value){
        this.getOperations().set(key, value);
    }
    public void set(String key, Object value, long time){
        this.getOperations().set(key, value,time, TimeUnit.SECONDS);
    }
    public void setWithDay(String key, Object value, long time){
        this.getOperations().set(key, value,time, TimeUnit.DAYS);
    }
    public Object get(String key){
        return this.getOperations().get(key);
    }
    public Object delete(String key){
        return this.redisTemplate.delete(key);
    }
}

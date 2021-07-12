package com.simple.common.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class RedisSubscriber extends MessageListenerAdapter {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message);
    }
}

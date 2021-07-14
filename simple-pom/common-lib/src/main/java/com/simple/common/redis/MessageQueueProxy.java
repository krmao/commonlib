package com.simple.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
public class MessageQueueProxy implements MessageQueueProvider {
    private final MessageQueueProvider provider;

    @Override
    public void registerMessageHandler(String topic, MessageQueueHandler listener) {
        this.provider.registerMessageHandler(topic,listener);
    }

    @Override
    public void sendMessage(String topic, Object message) {
        this.provider.sendMessage(topic,message);
    }

    @Override
    public void removeMessageHandler(String topic, MessageQueueHandler listener) {
        this.provider.removeMessageHandler(topic,listener);
    }
}

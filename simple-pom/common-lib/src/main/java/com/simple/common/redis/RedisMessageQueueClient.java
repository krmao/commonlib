package com.simple.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.HashMap;

@RequiredArgsConstructor
public class RedisMessageQueueClient implements MessageQueueProvider {

    private final RedisMessageListenerContainer messageListenerContainer;
    private final RedisTemplate<String, Object> redisTemplate;
    private  HashMap<String, RedisMessageQueueSubscriber> messageHandlers = new HashMap<String, RedisMessageQueueSubscriber>();

    public void registerMessageHandler(String topic, MessageQueueHandler listener){

        RedisMessageQueueSubscriber subscriber  = new RedisMessageQueueSubscriber(listener);
        this.messageHandlers.put(topic, subscriber);
        this.messageListenerContainer.addMessageListener(subscriber,new PatternTopic(topic));
    }
    public void removeMessageHandler(String topic, MessageQueueHandler listener){

        RedisMessageQueueSubscriber subscriber = this.messageHandlers.get(topic);
        this.messageListenerContainer.removeMessageListener(subscriber);
        this.messageHandlers.remove(topic);
    }
    public  void sendMessage(String topic, Object message){

        redisTemplate.convertAndSend(topic,message);
    }
}

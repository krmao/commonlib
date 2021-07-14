package com.simple.common.redis;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface MessageQueueProvider {
        public void registerMessageHandler(String topic, MessageQueueHandler listener);
        public  void sendMessage(String topic, Object message);
        public void removeMessageHandler(String topic, MessageQueueHandler listener);

}

package com.simple.common.redis;



public interface MessageQueueHandler {
    public void onMessage(String topic, String message);
}

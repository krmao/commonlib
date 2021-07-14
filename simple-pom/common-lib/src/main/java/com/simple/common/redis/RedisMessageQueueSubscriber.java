package com.simple.common.redis;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.nio.charset.Charset;

@RequiredArgsConstructor
public class RedisMessageQueueSubscriber extends MessageListenerAdapter {
    private final MessageQueueHandler handler;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(message.getChannel());
        topic  = StringEscapeUtils.unescapeJavaScript(topic);
        String patternString = new String(pattern);
        String recvMessage = new String(message.getBody());
        String resultString = StringEscapeUtils.unescapeJavaScript(recvMessage);
        resultString = resultString.substring(1, resultString.length() -1);
        System.out.println("Transfer string ---->" + resultString);
        this.handler.onMessage(topic, resultString);
        //System.out.println("Pattern String--->" + patternString);
    }
}

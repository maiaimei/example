package cn.maiaimei.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class GlobalChannelInterceptorB implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("===> GlobalChannelInterceptorB.preSend");
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("===> GlobalChannelInterceptorB.postSend");
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        log.info("===> GlobalChannelInterceptorB.afterSendCompletion");
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        log.info("===> GlobalChannelInterceptorB.preReceive");
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        log.info("===> GlobalChannelInterceptorB.postReceive");
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        log.info("===> GlobalChannelInterceptorB.afterReceiveCompletion");
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}

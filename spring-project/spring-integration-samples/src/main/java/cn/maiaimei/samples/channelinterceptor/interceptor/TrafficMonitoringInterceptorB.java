package cn.maiaimei.samples.channelinterceptor.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class TrafficMonitoringInterceptorB implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("===> TrafficMonitoringInterceptorB.preSend");
        return ChannelInterceptor.super.preSend(message, channel);
    }
}

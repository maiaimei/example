package cn.maiaimei.example.integration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.stream.ByteStreamReadingMessageSource;
import org.springframework.integration.stream.ByteStreamWritingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * 实现控制台的输入输出
 * https://docs.spring.io/spring-integration/docs/current/reference/html/stream.html#stream
 */
@Configuration
@ConditionalOnExpression(value = "false")
public class StreamSupportConfig {
    @Bean(name = "stdChannel")
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "stdChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<?> stdOutMessageSource() {
        return new ByteStreamReadingMessageSource(System.in, 1024);
    }

    @Bean
    @ServiceActivator(inputChannel = "stdChannel")
    public MessageHandler stdInMessageSource() {
        return new ByteStreamWritingMessageHandler(System.out, 1024);
    }
}

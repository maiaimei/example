package cn.maiaimei.samples.messagechannel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.*;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * https://docs.spring.io/spring-integration/docs/current/reference/html/core.html#channel-configuration
 */
@Configuration
public class MessageChannelConfig {

    @Bean
    public ThreadPoolTaskExecutor someExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public MessageChannel directChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel numberChannel() {
        DirectChannel channel = new DirectChannel();
        channel.setDatatypes(Number.class);
        return channel;
    }

    @Bean
    public MessageChannel queueChannel() {
        return new QueueChannel(25);
    }

    @Bean
    public MessageChannel priorityChannel() {
        return new PriorityChannel(20);
    }

    @Bean
    public MessageChannel pubsubChannel(ThreadPoolTaskExecutor someExecutor) {
        return new PublishSubscribeChannel(someExecutor);
    }

    @Bean
    public MessageChannel executorChannel(ThreadPoolTaskExecutor someExecutor) {
        return new ExecutorChannel(someExecutor);
    }
}

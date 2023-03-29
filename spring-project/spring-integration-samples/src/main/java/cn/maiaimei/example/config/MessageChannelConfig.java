package cn.maiaimei.example.config;

import cn.maiaimei.example.interceptor.TrafficMonitoringInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.*;
import org.springframework.messaging.MessageChannel;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://docs.spring.io/spring-integration/docs/current/reference/html/core.html#channel-configuration
 * https://docs.spring.io/spring-integration/docs/current/reference/html/core.html#channel-configuration-interceptors
 */
@Configuration
public class MessageChannelConfig {

    /**
     * <int:channel id="directChannel"/>
     */
    @Bean
    public MessageChannel directChannel() {
        return new DirectChannel();
    }

    /**
     * <int:channel id="numberChannel" datatype="java.lang.Number"/>
     */
    @Bean
    public MessageChannel numberChannel() {
        DirectChannel channel = new DirectChannel();
        channel.setDatatypes(Number.class);
        return channel;
    }

    /**
     * <int:channel id="queueChannel">
     * <queue capacity="25"/>
     * </int:channel>
     */
    @Bean
    public MessageChannel queueChannel() {
        return new QueueChannel(25);
    }

    /**
     * <int:publish-subscribe-channel id="pubsubChannel" task-executor="someExecutor"/>
     */
    @Bean
    public MessageChannel pubsubChannel() {
        final ExecutorService someExecutor = Executors.newFixedThreadPool(1);
        return new PublishSubscribeChannel(someExecutor);
    }

    /**
     * <int:channel id="executorChannel">
     * <int:dispatcher task-executor="someExecutor"/>
     * </int:channel>
     */
    @Bean
    public MessageChannel executorChannel() {
        final ExecutorService someExecutor = Executors.newFixedThreadPool(1);
        return new ExecutorChannel(someExecutor);
    }

    /**
     * <int:channel id="priorityChannel">
     * <int:priority-queue capacity="20"/>
     * </int:channel>
     */
    @Bean
    public MessageChannel priorityChannel() {
        return new PriorityChannel(20);
    }

    /**
     * <int:channel id="exampleChannel">
     * <int:interceptors>
     * <ref bean="trafficMonitoringInterceptor"/>
     * </int:interceptors>
     * </int:channel>
     */
    @Bean
    public QueueChannel exampleChannel(TrafficMonitoringInterceptor trafficMonitoringInterceptor) {
        final QueueChannel channel = new QueueChannel();
        channel.setInterceptors(Collections.singletonList(trafficMonitoringInterceptor));
        return channel;
    }
}

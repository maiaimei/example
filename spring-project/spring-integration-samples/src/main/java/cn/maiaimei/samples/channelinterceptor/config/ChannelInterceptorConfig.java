package cn.maiaimei.samples.channelinterceptor.config;

import cn.maiaimei.samples.channelinterceptor.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.GlobalChannelInterceptor;

import java.util.Arrays;

/**
 * https://docs.spring.io/spring-integration/docs/current/reference/html/core.html#channel-configuration-interceptors
 *
 * @GlobalChannelInterceptor 需要开启 @EnableIntegration
 */
@EnableIntegration
@Configuration
public class ChannelInterceptorConfig {

    @Bean
    public TrafficMonitoringInterceptor trafficMonitoringInterceptor() {
        return new TrafficMonitoringInterceptor();
    }

    @Bean
    public TrafficMonitoringInterceptorA trafficMonitoringInterceptorA() {
        return new TrafficMonitoringInterceptorA();
    }

    @Bean
    public TrafficMonitoringInterceptorB trafficMonitoringInterceptorB() {
        return new TrafficMonitoringInterceptorB();
    }

    @Bean
    @GlobalChannelInterceptor(patterns = "example*", order = 0)
    public GlobalChannelInterceptorA globalChannelInterceptorA() {
        return new GlobalChannelInterceptorA();
    }

    @Bean
    @GlobalChannelInterceptor(patterns = "example*", order = 1)
    public GlobalChannelInterceptorB globalChannelInterceptorB() {
        return new GlobalChannelInterceptorB();
    }

    @Bean
    @GlobalChannelInterceptor(patterns = "example*", order = 2)
    public GlobalChannelInterceptorC globalChannelInterceptorC() {
        return new GlobalChannelInterceptorC();
    }

    @Bean
    public QueueChannel exampleChannel(TrafficMonitoringInterceptor trafficMonitoringInterceptor,
                                       TrafficMonitoringInterceptorA trafficMonitoringInterceptorA,
                                       TrafficMonitoringInterceptorB trafficMonitoringInterceptorB) {
        final QueueChannel channel = new QueueChannel();
        channel.setInterceptors(Arrays.asList(trafficMonitoringInterceptorA, trafficMonitoringInterceptor, trafficMonitoringInterceptorB));
        return channel;
    }
}

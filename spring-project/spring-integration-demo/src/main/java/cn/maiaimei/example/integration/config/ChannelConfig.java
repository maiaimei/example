package cn.maiaimei.example.integration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
@ConditionalOnExpression(value = "false")
public class ChannelConfig {

    @Bean("inputChannel")
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean("queueChannel")
    public QueueChannel queueChannel() {
        return MessageChannels.queue().get();
    }

    @Bean
    public IntegrationFlow channelFlow() {
        return IntegrationFlows.from("inputChannel")
                .fixedSubscriberChannel()
                .channel("queueChannel")
                .get();
    }

}

package cn.maiaimei.example.integration.config;

import cn.maiaimei.example.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
@ConditionalOnExpression(value = "false")
@Import(value = {IntegrationService.class})
public class HelloIntegrationConfig {
    @Autowired
    IntegrationService integrationService;

    @Bean
    public MessageChannel beep() {
        return new QueueChannel(100);
    }

    @InboundChannelAdapter(value = "beep", poller = @Poller(fixedRate = "2000"))
    public long produce() {
        return integrationService.produce();
    }

    @ServiceActivator(inputChannel = "beep", poller = @Poller(fixedRate = "2000"))
    public void consume(Message<?> message) {
        integrationService.consume(message);
    }
}

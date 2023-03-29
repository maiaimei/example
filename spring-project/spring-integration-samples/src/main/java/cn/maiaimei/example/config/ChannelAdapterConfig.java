package cn.maiaimei.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;

import java.util.UUID;

@Slf4j
@Configuration
public class ChannelAdapterConfig {

    @Bean
    public DirectChannel channel3() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel channel4() {
        return new DirectChannel();
    }

    @InboundChannelAdapter(channel = "channel3", poller = @Poller(fixedRate = "${inbound-channel-adapter-fixed-rate}"))
    public String produce3() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 3 produce message: {}", payload);
        return payload;
    }

    @InboundChannelAdapter(channel = "channel4", poller = @Poller(cron = "${inbound-channel-adapter-cron}"))
    public String produce4() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 4 produce message: {}", payload);
        return payload;
    }

    @ServiceActivator(inputChannel = "channel3")
    public void consume3(Message<String> message) {
        log.info("<=== consumer 3 receive message: {}", message.getPayload());
    }

    @ServiceActivator(inputChannel = "channel4")
    public void consume4(String payload) {
        log.info("<=== consumer 4 receive message: {}", payload);
    }

}

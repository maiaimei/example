package cn.maiaimei.samples.channeladapter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.messaging.Message;

import java.util.UUID;

@Slf4j
public class ChannelAdapterConfig {

    @Bean
    public DirectChannel channel1() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel channel2() {
        return new DirectChannel();
    }

    /**
     * inbound-channel-adapter
     * {@link SourcePollingChannelAdapter}
     */
    @InboundChannelAdapter(channel = "channel1", poller = @Poller(fixedRate = "5000"))
    public String produce1() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 1 produce message: {}", payload);
        return payload;
    }

    @InboundChannelAdapter(channel = "channel2", poller = @Poller(cron = "0/5 * * * * ?"))
    public String produce2() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 2 produce message: {}", payload);
        return payload;
    }

    /**
     * outbound-channel-adapter
     */
    @ServiceActivator(inputChannel = "channel1")
    public void consume1(Message<String> message) {
        log.info("<=== consumer 1 receive message: {}", message.getPayload());
    }

    @ServiceActivator(inputChannel = "channel2")
    public void consume2(String payload) {
        log.info("<=== consumer 2 receive message: {}", payload);
    }

}

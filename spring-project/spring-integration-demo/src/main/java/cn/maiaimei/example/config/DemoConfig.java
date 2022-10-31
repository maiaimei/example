package cn.maiaimei.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Random;

@Slf4j
//@Configuration
public class DemoConfig {
    @Bean
    public MessageChannel beep() {
        return new QueueChannel();
    }

    @InboundChannelAdapter(value = "beep", poller = @Poller(fixedRate = "2000"))
    public int input() {
        Random random = new Random();
        log.info("===> Hello, Spring Integration");
        return random.nextInt();
    }

    @ServiceActivator(inputChannel = "beep", poller = @Poller(fixedRate = "2000"))
    public void output(Message<?> message) {
        log.info("<=== " + message);
    }
}

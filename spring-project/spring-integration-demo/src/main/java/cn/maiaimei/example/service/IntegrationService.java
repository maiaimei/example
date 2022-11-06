package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.util.Random;

@Slf4j
public class IntegrationService {
    public long produce() {
        Random random = new Random();
        long i = Math.abs(random.nextLong());
        log.info("===> Hello Integration {}", i);
        return i;
    }

    public void consume(Message<?> message) {
        log.info("<=== Hello Integration {}", message.getPayload());
    }
}

package cn.maiaimei.samples.channeladapter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

@Slf4j
public class ConsumerService {

    public void consume1(Message<String> message) {
        log.info("<=== consumer 1 receive message: {}", message.getPayload());
    }

    public void consume2(String payload) {
        log.info("<=== consumer 2 receive message: {}", payload);
    }
    
}

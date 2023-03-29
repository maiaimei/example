package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    public void consume1(Message<String> message) {
        log.info("<=== consumer 1 receive message: {}", message.getPayload());
    }

    public void consume2(String payload) {
        log.info("<=== consumer 2 receive message: {}", payload);
    }

}

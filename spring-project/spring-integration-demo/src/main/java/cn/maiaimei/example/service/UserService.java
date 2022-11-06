package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

@Slf4j
public class UserService {
    public void get(Message<?> message) {
        Object payload = message.getPayload();
        log.info("{}", payload);
    }
}

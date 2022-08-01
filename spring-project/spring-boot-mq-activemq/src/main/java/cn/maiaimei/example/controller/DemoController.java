package cn.maiaimei.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {
    private static final String DESTINATION_NAME = "hello_activemq";

    @Autowired
    JmsTemplate jmsTemplate;

    @GetMapping("/send")
    public void send(@RequestParam String message) {
        jmsTemplate.convertAndSend(DESTINATION_NAME, message);
    }

    @JmsListener(destination = DESTINATION_NAME)
    public void receive(String content) {
        log.info("{}", content);
    }
}

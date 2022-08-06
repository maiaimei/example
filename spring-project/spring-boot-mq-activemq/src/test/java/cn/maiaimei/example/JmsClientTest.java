package cn.maiaimei.example;

import cn.maiaimei.example.utils.JmsClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class JmsClientTest extends TestBase {
    @Autowired
    JmsClient jmsClient;

    @Test
    void testSend() {
        jmsClient.send(DEFAULT_QUEUE_NAME, DEFAULT_MESSAGE);
    }

    @Test
    void testReceive() {
        jmsClient.receive(DEFAULT_QUEUE_NAME);
    }

    @Test
    void testSendAndReceive() {
        List<String> messages = Arrays.asList("One", "Two", "Three", "Four", "Five");
        List<String> correlationIds = new ArrayList<>();
        for (String message : messages) {
            String correlationId = jmsClient.send(DEFAULT_QUEUE_NAME, message);
            correlationIds.add(correlationId);
        }
        for (String correlationId : correlationIds) {
            String receiveMessage = jmsClient.receiveByCorrelationId(DEFAULT_QUEUE_NAME, correlationId);
            log.info("correlationId: {}, message: {}", correlationId, receiveMessage);
        }

        jmsClient.send(DEFAULT_QUEUE_NAME, "Hi, ActiveMQ!", "Hi", "ActiveMQ");
        String receiveMessage = jmsClient.receiveSelected(DEFAULT_QUEUE_NAME, "Hi = 'ActiveMQ'");
        log.info("message: {}", receiveMessage);
    }
}

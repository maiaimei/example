package cn.maiaimei.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;
import org.springframework.util.CollectionUtils;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 同步收发
 * {@link JmsOperations}
 * {@link JmsTemplate}
 */
@Slf4j
@SpringBootTest
public class JmsOperationsTest extends TestBase {
    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    void initData() {
        for (int i = 1; i <= 5; i++) {
            jmsTemplate.convertAndSend(DEFAULT_QUEUE_NAME, String.format("%s%s", DEFAULT_MESSAGE, i));
        }
    }

    @SneakyThrows
    @Test
    void testSendReceive() {
        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            TextMessage message = session.createTextMessage();
            message.setText(DEFAULT_MESSAGE);
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        TextMessage message = (TextMessage) receiveMessage;
        assertEquals(DEFAULT_MESSAGE, message.getText());
    }

    @SneakyThrows
    @Test
    void testConvertSendReceive() {
        jmsTemplate.convertAndSend(DEFAULT_QUEUE_NAME, DEFAULT_MESSAGE);

        Object receiveMessage = jmsTemplate.receiveAndConvert(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        assertEquals(DEFAULT_MESSAGE, receiveMessage);
    }

    @SneakyThrows
    @Test
    void testBrowse() {
        List<String> resultMessages = jmsTemplate.browse(DEFAULT_QUEUE_NAME, (session, browser) -> {
            List<String> messages = new ArrayList<>();
            Enumeration<Message> messageEnumeration = browser.getEnumeration();
            while (messageEnumeration.hasMoreElements()) {
                Message browserMessage = messageEnumeration.nextElement();
                if (browserMessage instanceof TextMessage) {
                    TextMessage message = (TextMessage) browserMessage;
                    messages.add(message.getText());
                }
            }
            return messages;
        });
        if (!CollectionUtils.isEmpty(resultMessages)) {
            for (String message : resultMessages) {
                log.info("{}", message);
            }
        }
    }

    @SneakyThrows
    @Test
    void testExecuteBrowse() {
        jmsTemplate.execute(session -> {
            QueueBrowser browser = session.createBrowser(new ActiveMQQueue(DEFAULT_QUEUE_NAME));
            Enumeration<Message> messageEnumeration = browser.getEnumeration();

            while (messageEnumeration.hasMoreElements()) {
                Message browserMessage = messageEnumeration.nextElement();
                if (browserMessage instanceof TextMessage) {
                    TextMessage message = (TextMessage) browserMessage;
                    log.info("message: {}", message.getText());
                }
            }

            browser.close();
            return null;
        });
    }

    @SneakyThrows
    @Test
    void testExecuteSend() {
        jmsTemplate.execute(session -> {
            ActiveMQQueue queue = new ActiveMQQueue(DEFAULT_QUEUE_NAME);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(DEFAULT_MESSAGE);
            try {
                producer.send(queue, message);
            } finally {
                JmsUtils.closeMessageProducer(producer);
            }
            return null;
        });
    }

    @SneakyThrows
    @Test
    void testExecuteReceive() {
        String msg = jmsTemplate.execute(session -> {
            String result = null;
            ActiveMQQueue queue = new ActiveMQQueue(DEFAULT_QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(queue);
            try {
                Message receiveMessage = consumer.receive();
                if (receiveMessage instanceof TextMessage) {
                    TextMessage message = (TextMessage) receiveMessage;
                    result = message.getText();
                }
            } finally {
                JmsUtils.closeMessageConsumer(consumer);
            }
            return result;
        });
        log.info("{}", msg);
    }

    @Test
    void testTopicProducer() {
        jmsTemplate.execute(session -> {
            ActiveMQTopic topic = new ActiveMQTopic(DEFAULT_TOPIC_NAME);
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(DEFAULT_MESSAGE);
            try {
                producer.send(topic, message);
            } finally {
                JmsUtils.closeMessageProducer(producer);
            }
            return null;
        });
    }

    @Test
    void testTopicConsumer() {
        String msg = jmsTemplate.execute(session -> {
            String result = null;
            ActiveMQTopic topic = new ActiveMQTopic(DEFAULT_TOPIC_NAME);
            MessageConsumer consumer = session.createConsumer(topic);
            Message receiveMessage = consumer.receive();
            if (receiveMessage instanceof TextMessage) {
                TextMessage message = (TextMessage) receiveMessage;
                result = message.getText();
            }
            return result;
        });
        log.info("{}", msg);
    }
}

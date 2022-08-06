package cn.maiaimei.example;

import cn.maiaimei.example.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JMS Message Type
 * https://blog.csdn.net/superick/article/details/23367377/
 */
@SpringBootTest
class MessageTypeTest extends TestBase {
    @Autowired
    JmsTemplate jmsTemplate;

    @SneakyThrows
    @Test
    void testSendAndReceiveTextMessage() {
        String msg = "Hello, ActiveMQ, this is a TextMessage.";

        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            TextMessage message = session.createTextMessage();
            message.setText(msg);
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        TextMessage message = (TextMessage) receiveMessage;
        assertEquals(msg, message.getText());
    }

    @SneakyThrows
    @Test
    void testSendAndReceiveBytesMessage() {
        String msg = "Hello, ActiveMQ, this is a TextMessage.";

        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(msg.getBytes());
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);

        // 解包BytesMessage
        BytesMessage message = (BytesMessage) receiveMessage;
        int length = Integer.parseInt(String.valueOf(message.getBodyLength()));
        byte[] bytes = new byte[length];
        message.readBytes(bytes);
        String actualMessage = new String(bytes);

        assertEquals(msg, actualMessage);
    }

    @SneakyThrows
    @Test
    void testSendAndReceiveMapMessage() {
        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            MapMessage message = session.createMapMessage();
            message.setString("name", "Amy");
            message.setChar("sex", 'F');
            message.setInt("age", 33);
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        MapMessage message = (MapMessage) receiveMessage;
        assertEquals("Amy", message.getString("name"));
        assertEquals('F', message.getChar("sex"));
        assertEquals(33, message.getInt("age"));
    }

    @SneakyThrows
    @Test
    void testSendAndReceiveStreamMessage() {
        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            StreamMessage message = session.createStreamMessage();
            message.writeString("Amy");
            message.writeChar('F');
            message.writeInt(33);
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        StreamMessage message = (StreamMessage) receiveMessage;
        assertEquals("Amy", message.readString());
        assertEquals('F', message.readChar());
        assertEquals(33, message.readInt());
    }

    @SneakyThrows
    @Test
    void testSendAndReceiveObjectMessage() {
        User user = User.builder().nickname("初冬十月").username("Amy").build();

        jmsTemplate.send(DEFAULT_QUEUE_NAME, session -> {
            ObjectMessage message = session.createObjectMessage();
            message.setObject(user);
            return message;
        });

        Message receiveMessage = jmsTemplate.receive(DEFAULT_QUEUE_NAME);
        assertNotNull(receiveMessage);
        ObjectMessage message = (ObjectMessage) receiveMessage;
        assertEquals(user, (User) message.getObject());
    }
}

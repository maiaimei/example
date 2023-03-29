package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Message}
 */
class MessageTest {

    @Test
    void testGenericMessage() {
        String payload = "Hello GenericMessage";
        final GenericMessage<String> message = new GenericMessage<>(payload);
        assertEquals(payload, message.getPayload());
    }

    @Test
    void testMessageBuilder() {
        String payload = "Hello MessageBuilder";
        final Message<String> message = MessageBuilder.withPayload(payload).build();
        assertEquals(payload, message.getPayload());
    }

}

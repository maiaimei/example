package cn.maiaimei.example;

import cn.maiaimei.example.config.TestConfg;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link MessageChannel}
 * {@link PollableChannel}: {@link QueueChannel}
 * {@link SubscribableChannel}: {@link DirectChannel} {@link PublishSubscribeChannel}
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfg.class)
class MessageChannelTest {

    @Autowired
    QueueChannel exampleChannel;

    @Autowired
    DirectChannel exampleChannelB;

    @Test
    void testChannelInterceptor() {
        final Message<String> message = MessageBuilder.withPayload("Hello ChannelInterceptor").build();
        exampleChannel.send(message);
        final Message<?> receiveMessage = exampleChannel.receive();
        assertEquals(message.getPayload(), receiveMessage.getPayload());
    }

    @Test
    void testChannelInterceptor4ExampleChannelB() {
        final Message<String> message = MessageBuilder.withPayload("Hello ChannelInterceptor").build();
        exampleChannelB.subscribe(receiveMsg -> {
            assertEquals(message.getPayload(), receiveMsg.getPayload());
        });
        exampleChannelB.send(message);
    }

    @Test
    void testQueueChannel() {
        final Message<String> message = MessageBuilder.withPayload("Hello QueueChannel").build();
        final QueueChannel channel = new QueueChannel();
        channel.send(message);
        final Message<?> receiveMessage = channel.receive();
        assertEquals(message.getPayload(), receiveMessage.getPayload());
    }

    @Test
    void testDirectChannel() {
        final Message<String> message = MessageBuilder.withPayload("Hello QueueChannel").build();
        final MessageHandler messageHandler = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> receiveMessage) throws MessagingException {
                assertEquals(message.getPayload(), receiveMessage.getPayload());
            }
        };
        final DirectChannel channel = new DirectChannel();
        channel.subscribe(messageHandler);
        channel.send(message);
    }

    @Test
    void testPublishSubscribeChannel() {
        final Message<String> message = MessageBuilder.withPayload("Hello QueueChannel").build();
        final MessageHandler messageHandlerA = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> receiveMessage) throws MessagingException {
                log.info("A receive message: {}", receiveMessage.getPayload());
                assertEquals(message.getPayload(), receiveMessage.getPayload());
            }
        };
        final MessageHandler messageHandlerB = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> receiveMessage) throws MessagingException {
                log.info("B receive message: {}", receiveMessage.getPayload());
                assertEquals(message.getPayload(), receiveMessage.getPayload());
            }
        };
        final PublishSubscribeChannel channel = new PublishSubscribeChannel();
        channel.subscribe(messageHandlerA);
        channel.subscribe(messageHandlerB);
        channel.send(message);
    }

}

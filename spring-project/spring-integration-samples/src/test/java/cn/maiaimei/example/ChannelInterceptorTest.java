package cn.maiaimei.example;

import cn.maiaimei.samples.channelinterceptor.ChannelInterceptorAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChannelInterceptorAutoConfiguration.class)
public class ChannelInterceptorTest {

    @Autowired
    QueueChannel exampleChannel;

    @Test
    void testChannelInterceptor() {
        final Message<String> message = MessageBuilder.withPayload("Hello ChannelInterceptor").build();
        exampleChannel.send(message);
        final Message<?> receiveMessage = exampleChannel.receive();
        assertEquals(message.getPayload(), receiveMessage.getPayload());
    }

}

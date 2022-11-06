package cn.maiaimei.example.integration.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Objects;

@SpringBootTest
public class ChannelConfigTest {

    @Autowired
    private MessageChannel inputChannel;

    @Autowired
    private QueueChannel queueChannel;

    /**
     * 消息发送与接收
     */
    @Test
    public void testSendAndReceive() {
        String payload = "DOG:foo";
        Message<String> msg = MessageBuilder.withPayload(payload).build();
        inputChannel.send(msg);
        Message<?> outMsg = queueChannel.receive();
        assert Objects.requireNonNull(outMsg).getPayload().toString().equals("DOG:foo");
    }

}

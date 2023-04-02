package cn.maiaimei.samples.messagechannel;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@Import(MessageChannelConfig.class)
@ImportResource(locations = "classpath:beans/message-channel.xml")
public class MessageChannelAutoConfiguration {
}

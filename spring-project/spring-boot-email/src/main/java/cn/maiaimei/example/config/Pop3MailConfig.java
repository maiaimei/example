package cn.maiaimei.example.config;

import cn.maiaimei.example.util.MailHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.MailReceivingMessageSource;
import org.springframework.integration.mail.Pop3MailReceiver;
import org.springframework.messaging.MessageChannel;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@EnableIntegration
@Configuration
public class Pop3MailConfig {
    @Bean
    public MessageChannel receiveEmailChannel() {
        return new DirectChannel();
    }

    @Bean
    public Properties javaMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.store.protocol", "pop3");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.port", "995");
        properties.setProperty("mail.pop3.port", "995");
        return properties;
    }

    @Bean
    public Pop3MailReceiver pop3MailReceiver() {
        String url = "pop3://1211674185:[pop3_password]@pop.qq.com/INBOX";
        Pop3MailReceiver mailReceiver = new Pop3MailReceiver(url);
        mailReceiver.setJavaMailProperties(javaMailProperties());
        mailReceiver.setShouldDeleteMessages(Boolean.FALSE);
        // TODO: Resolve java.lang.IllegalStateException: Folder is not Open
        mailReceiver.setAutoCloseFolder(Boolean.FALSE);
        return mailReceiver;
    }

    @Bean
    @InboundChannelAdapter(channel = "receiveEmailChannel", poller = @Poller(fixedDelay = "10000"))
    public MailReceivingMessageSource mailReceivingMessageSource() {
        return new MailReceivingMessageSource(pop3MailReceiver());
    }

    @ServiceActivator(inputChannel = "receiveEmailChannel")
    public void handleMessage(MimeMessage message) {
        MailHelper.printMessage(message);
    }
}

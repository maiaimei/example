package cn.maiaimei.example.config;

import cn.maiaimei.example.util.MailHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.messaging.MessageChannel;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@EnableIntegration
@Configuration
public class ImapMailConfig {
    @Bean
    public MessageChannel receiveEmailChannel() {
        return new DirectChannel();
    }

    @Bean
    public Properties javaMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.ssl", "true");
        return properties;
    }

    @Bean
    public ImapMailReceiver imapMailReceiver() {
        String url = "imaps://1211674185:[imap_password]@imap.qq.com:993/inbox";
        ImapMailReceiver mailReceiver = new ImapMailReceiver(url);
        mailReceiver.setJavaMailProperties(javaMailProperties());
        // shouldMarkMessagesAsRead需要设置为true，否则新邮件会一直刷屏
        mailReceiver.setShouldMarkMessagesAsRead(Boolean.TRUE);
        mailReceiver.setShouldDeleteMessages(Boolean.FALSE);
        // TODO: setSearchTermStrategy
//        mailReceiver.setSearchTermStrategy(new SearchTermStrategy() {
//            @Override
//            public SearchTerm generateSearchTerm(Flags supportedFlags, Folder folder) {
//                return new AndTerm(new FromStringTerm("maiaimei"), new BodyTerm("test"));
//            }
//        });
        mailReceiver.setAutoCloseFolder(Boolean.FALSE);
        return mailReceiver;
    }

    @Bean
    public ImapIdleChannelAdapter imapIdleChannelAdapter() {
        ImapIdleChannelAdapter imapIdleChannelAdapter = new ImapIdleChannelAdapter(imapMailReceiver());
        imapIdleChannelAdapter.setOutputChannel(receiveEmailChannel());
        imapIdleChannelAdapter.setAutoStartup(Boolean.TRUE);
        return imapIdleChannelAdapter;
    }

    @ServiceActivator(inputChannel = "receiveEmailChannel")
    public void handleMessage(MimeMessage message) throws IOException, MessagingException {
        MailHelper.printMessage(message);
    }
}

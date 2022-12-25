package cn.maiaimei.example;

import cn.maiaimei.example.config.ImapMailConfig;
import cn.maiaimei.example.config.Pop3MailConfig;
import cn.maiaimei.example.util.MailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.mail.internet.MimeMessage;

public class SpringIntegrationMailTest {
    private static final Logger log = LoggerFactory.getLogger(MailHelper.class);

    public static void main(String[] args) {
        //receiveEmailByPop3_1();
        //receiveEmailByPop3_2();

        //receiveEmailByImap_1();
        receiveEmailByImap_2();
    }

    private static void receiveEmailByPop3_1() {
        receiveEmail("/META-INF/spring/integration/qq-pop3-config.xml");
    }

    private static void receiveEmailByImap_1() {
        receiveEmail("/META-INF/spring/integration/qq-imap-idle-config.xml");
    }

    private static void receiveEmailByPop3_2() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Pop3MailConfig.class);
    }

    private static void receiveEmailByImap_2() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImapMailConfig.class);
    }

    private static void receiveEmail(String configLocation) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
        DirectChannel inputChannel = applicationContext.getBean("receiveEmailChannel", DirectChannel.class);
        inputChannel.subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                log.info("Received Message: " + message);
                MailHelper.printMessage((MimeMessage) message.getPayload());
            }
        });
    }
}

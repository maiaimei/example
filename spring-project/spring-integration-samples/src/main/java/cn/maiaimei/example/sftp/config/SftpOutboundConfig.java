package cn.maiaimei.example.sftp.config;

import cn.maiaimei.example.sftp.SftpGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.util.UUID;

/**
 * upload file to sftp server
 * see {@link SftpGateway}
 */
@Configuration
public class SftpOutboundConfig {
    @Value("${sftp.remoteDirectory}")
    private String remoteDirectory;

    @Autowired
    private SessionFactory sftpSessionFactory;

    @Bean
    public QueueChannel toSftpChannel() {
        return new QueueChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public MessageHandler handler() {
        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory);
        handler.setRemoteDirectoryExpression(new LiteralExpression(remoteDirectory));
        handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                File uploadFile = (File) message.getPayload();
                String uploadFileSuffix = uploadFile.getName().substring(uploadFile.getName().lastIndexOf("."));
                return UUID.randomUUID().toString().replaceAll("-", "").concat(uploadFileSuffix);
            }
        });
        return handler;
    }
}

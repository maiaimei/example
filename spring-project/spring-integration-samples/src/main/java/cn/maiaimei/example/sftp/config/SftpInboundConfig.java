package cn.maiaimei.example.sftp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.File;

/**
 * download file from sftp server
 */
@Configuration
public class SftpInboundConfig {
    private static final Logger log = LoggerFactory.getLogger(SftpInboundConfig.class);

    @Value("${sftp.remoteDirectory}")
    private String remoteDirectory;

    @Value("${sftp.localDirectory}")
    private String localDirectory;

    @Autowired
    private SessionFactory sftpSessionFactory;

    @Bean
    public QueueChannel fromSftpChannel() {
        return new QueueChannel();
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
        SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory);
        fileSynchronizer.setRemoteDirectory(remoteDirectory);
        fileSynchronizer.setDeleteRemoteFiles(Boolean.TRUE);
        fileSynchronizer.setFilter(new SftpSimplePatternFileListFilter("*.jpeg"));
        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(channel = "fromSftpChannel", poller = @Poller(fixedRate = "5000"))
    public MessageSource sftpMessageSource() {
        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer());
        source.setLocalDirectory(new File(localDirectory));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        source.setMaxFetchSize(1);
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "fromSftpChannel")
    public MessageHandler handlerOrder() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message message) throws MessagingException {
                log.info("{}", message.getPayload());
            }
        };
    }
}

package cn.maiaimei.example.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

/**
 * https://docs.spring.io/spring-integration/docs/current/reference/html/file.html#files
 */
@Configuration
@ConditionalOnExpression(value = "false")
public class FileCopyConfig {
    @Value("${file-copy.input-directory}")
    private String inputDir;
    @Value("${file-copy.output-directory}")
    private String outDir;
    @Value("${file-copy.filename-pattern}")
    private String fileNamePattern;

    @Bean(name = "fileChannel")
    public MessageChannel fileChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        // A FileReadingMessageSource can be used to consume files from the filesystem. 
        // This is an implementation of MessageSource that creates messages from a file system directory. 
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(inputDir));
        sourceReader.setFilter(new SimplePatternFileListFilter(fileNamePattern));
        return sourceReader;
    }

    @Bean
    @ServiceActivator(inputChannel = "fileChannel")
    public MessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(outDir));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        return handler;
    }
}


package cn.maiaimei.example.sftp;

import cn.maiaimei.example.sftp.config.SftpOutboundConfig;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;

/**
 * see {@link SftpOutboundConfig}
 */
@MessagingGateway
public interface SftpGateway {
    @Gateway(requestChannel = "toSftpChannel")
    void sendToSftp(File file);
}

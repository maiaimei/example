package cn.maiaimei.example.sftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

@EnableScheduling
@IntegrationComponentScan
@SpringBootApplication
public class SftpApplication {
    @Autowired
    private SftpGateway sftpGateway;

    public static void main(String[] args) {
        new SpringApplicationBuilder(SftpApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendToSftp() {
        sftpGateway.sendToSftp(new File("E:/tmp/1.jpeg"));
    }
}

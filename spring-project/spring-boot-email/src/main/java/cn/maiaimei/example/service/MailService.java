package cn.maiaimei.example.service;

import cn.maiaimei.example.pojo.MailRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MailService {
    /**
     * 简单文本邮件
     */
    void sendSimpleMail(MailRequest mailRequest);

    /**
     * Html格式邮件，可带附件
     */
    void sendHtmlMail(MailRequest mailRequest, MultipartFile[] attachments);

    void sendTplMail(String to, Map<String, Object> orderInfo);
}

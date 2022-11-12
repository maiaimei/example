package cn.maiaimei.example.controller;

import cn.maiaimei.example.pojo.MailRequest;
import cn.maiaimei.example.service.MailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Api
@RestController
public class MailController {
    @Autowired
    MailService mailService;

    @PostMapping("/sendSimpleMail")
    public void sendSimpleMail(@RequestBody MailRequest mailRequest) {
        mailService.sendSimpleMail(mailRequest);
    }

    @PostMapping(value = "/sendHtmlMail", headers = "content-type=multipart/form-data")
    public void sendHtmlMail(MailRequest mailRequest, @RequestPart(name = "attachments") MultipartFile[] attachments) {
        mailService.sendHtmlMail(mailRequest, attachments);
    }

    /**
     * swagger上传文件需要设置headers = "content-type=multipart/form-data"
     */
    @PostMapping(value = "/uploadFile", headers = "content-type=multipart/form-data")
    public void uploadFile(@RequestPart(name = "attachments") MultipartFile[] attachments) {
        for (MultipartFile attachment : attachments) {
            System.out.println(attachment.getOriginalFilename());
        }
    }

    @PostMapping("/sendTplMail")
    public void sendTplMail(String to, @RequestBody Map<String, Object> orderInfo) {
        mailService.sendTplMail(to, orderInfo);
    }
}

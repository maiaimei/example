package cn.maiaimei.example.service.impl;

import cn.maiaimei.example.pojo.MailRequest;
import cn.maiaimei.example.service.MailService;
import cn.maiaimei.example.util.FreemarkerHelper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Resource
    private FreemarkerHelper freemarkerHelper;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void validMailRequest(MailRequest mailRequest) {
        Assert.notNull(mailRequest, "邮件请求不能为空");
        Assert.notNull(mailRequest.getTo(), "邮件收件人不能为空");
        Assert.notNull(mailRequest.getSubject(), "邮件主题不能为空");
        Assert.notNull(mailRequest.getText(), "邮件正文不能为空");
    }

    @Override
    public void sendSimpleMail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        validMailRequest(mailRequest);
        // 邮件发件人
        message.setFrom(from);
        // 邮件收件人 1或多个
        message.setTo(mailRequest.getTo().toArray(new String[0]));
        // 邮件抄送人 1或多个
        if (!CollectionUtils.isEmpty(mailRequest.getCc())) {
            message.setCc(mailRequest.getCc().toArray(new String[0]));
        }
        // 邮件密送人 1或多个
        if (!CollectionUtils.isEmpty(mailRequest.getBcc())) {
            message.setBcc(mailRequest.getBcc().toArray(new String[0]));
        }
        // 邮件主题
        message.setSubject(mailRequest.getSubject());
        // 邮件内容
        message.setText(mailRequest.getText());
        // 邮件发送时间
        message.setSentDate(new Date());
        javaMailSender.send(message);
        logger.info("邮件发送成功：{}->{}", from, String.join(",", mailRequest.getTo()));
    }

    @SneakyThrows
    @Override
    public void sendHtmlMail(MailRequest mailRequest, MultipartFile[] attachments) {
        MimeMessage message = javaMailSender.createMimeMessage();
        validMailRequest(mailRequest);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 邮件发件人
        helper.setFrom(from);
        // 邮件收件人 1或多个
        helper.setTo(mailRequest.getTo().toArray(new String[0]));
        // 邮件抄送人 1或多个
        if (!CollectionUtils.isEmpty(mailRequest.getCc())) {
            helper.setCc(mailRequest.getCc().toArray(new String[0]));
        }
        // 邮件密送人 1或多个
        if (!CollectionUtils.isEmpty(mailRequest.getBcc())) {
            helper.setBcc(mailRequest.getBcc().toArray(new String[0]));
        }
        // 邮件主题
        helper.setSubject(mailRequest.getSubject());
        // 邮件内容
        helper.setText(mailRequest.getText(), Boolean.TRUE);
        // 邮件发送时间
        helper.setSentDate(new Date());
        // 附件
        if (attachments != null && attachments.length > 0) {
            for (MultipartFile attachment : attachments) {
                helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
            }
        }
        javaMailSender.send(message);
        logger.info("邮件发送成功：{}->{}", from, String.join(",", mailRequest.getTo()));
    }

    @SneakyThrows
    @Override
    public void sendTplMail(String to, Map<String, Object> orderInfo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 邮件发件人
        helper.setFrom(from);
        // 邮件收件人 1或多个
        helper.setTo(to);
        // 邮件主题
        helper.setSubject(String.format("京东已收到您的订单【%s】，欢迎您随时关注订单状态！", orderInfo.get("orderNo")));
        // 邮件内容
        helper.setText(freemarkerHelper.processTemplateIntoString("jd.ftl", orderInfo), Boolean.TRUE);
        // 邮件发送时间
        helper.setSentDate(new Date());
        javaMailSender.send(message);
        logger.info("邮件发送成功：{}->{}", from, to);
    }
}


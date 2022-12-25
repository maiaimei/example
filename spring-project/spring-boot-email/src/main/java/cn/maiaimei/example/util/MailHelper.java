package cn.maiaimei.example.util;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.HashSet;
import java.util.Set;

public class MailHelper {
    private static final Logger log = LoggerFactory.getLogger(MailHelper.class);

    private static final Set<String> RECEIVED_MAIL_SET = new HashSet<>();

    /**
     * 解析邮件
     */
    @SneakyThrows
    public static void printMessage(MimeMessage message) {
        if (RECEIVED_MAIL_SET.contains(message.getMessageID())) {
            log.info("MessageID: {}, {} 已接收！", message.getMessageID(), getSubject(message));
            return;
        }
        RECEIVED_MAIL_SET.add(message.getMessageID());
        log.info("MessageID: {}", message.getMessageID());
        log.info("MessageNumber: {}", message.getMessageNumber());
        log.info("邮件主题: {}", getSubject(message));
        log.info("邮件内容: {}", getContent(message));
        log.info("发件人: {}", getFrom(message));
        log.info("发送日期: {}", message.getSentDate());
    }

    /**
     * 获取邮件主题
     */
    @SneakyThrows
    public static String getSubject(Message message) {
        return MimeUtility.decodeText(message.getSubject());
    }

    /**
     * 获取邮件正文
     */
    @SneakyThrows
    public static String getContent(MimeMessage message) {
        if (!message.isMimeType("multipart/*")) {
            return (String) message.getContent();
        }
        Multipart multipart = (Multipart) message.getContent();
        for (int i = 0; i < multipart.getCount(); i++) {
            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
            if (part.isMimeType("text/*") && !BodyPart.ATTACHMENT.equals(part.getDisposition())) {
                return (String) part.getContent();
            }
        }
        return "";
    }

    @SneakyThrows
    public static String getFrom(MimeMessage message) {
        Address[] froms = message.getFrom();
        InternetAddress address = (InternetAddress) froms[0];
        String personal = address.getPersonal();
        return personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
    }
}

package cn.maiaimei.example.controller;

import cn.maiaimei.example.constants.MQConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Api
@Slf4j
@RestController
public class MQController {
    @Autowired
    JmsTemplate jmsTemplate;
    
    @GetMapping("/send")
    public void send(@RequestParam String message) {
        jmsTemplate.convertAndSend(MQConstants.DESTINATION_NAME_MY_QUEUE, message);
    }

    /**
     * request-response/request-reply 请求响应/请求回复模式
     */
    @SneakyThrows
    @ApiOperation(value = "请求响应模式")
    @GetMapping("/requestAndResponse")
    public String requestAndResponse(@RequestParam final String message) {
        AtomicReference<Message> messageRef = new AtomicReference<>();

        jmsTemplate.send(MQConstants.OUT_QUEUE, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            // 设置回复队列
            textMessage.setJMSReplyTo(new ActiveMQQueue(MQConstants.IN_QUEUE));
            // 设置关联ID
            textMessage.setJMSCorrelationID(UUID.randomUUID().toString().replaceAll("-", ""));
            messageRef.set(textMessage);
            return textMessage;
        });

        String filter = "JMSCorrelationID = '" + messageRef.get().getJMSCorrelationID() + "'";
        Object receiveObject = jmsTemplate.receiveSelectedAndConvert(MQConstants.IN_QUEUE, filter);
        return Objects.requireNonNull(receiveObject).toString();
    }

    /**
     * request-response/request-reply 请求响应/请求回复模式
     */
    @SneakyThrows
    @JmsListener(destination = MQConstants.OUT_QUEUE)
    public void reply(TextMessage message) {
        log.info("JMSMessageID is {}, JMSCorrelationID is {}, Text is {}", message.getJMSMessageID(), message.getJMSCorrelationID(), message.getText());
        jmsTemplate.send(MQConstants.IN_QUEUE, session -> {
            TextMessage textMessage = session.createTextMessage(message.getText() + " reply ok");
            // 设置关联ID
            textMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            return textMessage;
        });
    }

    @JmsListener(destination = MQConstants.DESTINATION_NAME_MY_QUEUE)
    public void receive(String content) {
        log.info("{}", content);
    }
}

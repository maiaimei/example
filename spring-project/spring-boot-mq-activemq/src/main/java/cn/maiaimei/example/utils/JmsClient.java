package cn.maiaimei.example.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Map;
import java.util.UUID;

@Component
public class JmsClient {
    @Autowired
    JmsTemplate jmsTemplate;

    /**
     * 发送消息
     *
     * @param destinationName 目的地名称
     * @param message         消息
     * @return correlationId
     */
    public String send(String destinationName, final String message) {
        final String correlationId = getCorrelationId();
        jmsTemplate.send(destinationName, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setJMSCorrelationID(correlationId);
            return textMessage;
        });
        return correlationId;
    }

    /**
     * 发送消息
     *
     * @param destinationName 目的地名称
     * @param message         消息
     * @param propertyName    消息属性键
     * @param propertyValue   消息属性值
     * @return correlationId
     */
    public String send(String destinationName, final String message, final String propertyName, final String propertyValue) {
        final String correlationId = getCorrelationId();
        jmsTemplate.send(destinationName, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setJMSCorrelationID(correlationId);
            textMessage.setStringProperty(propertyName, propertyValue);
            return textMessage;
        });
        return correlationId;
    }

    /**
     * 发送消息
     *
     * @param destinationName 目的地名称
     * @param message         消息
     * @param propertyMap     消息属性
     * @return correlationId
     */
    public String send(String destinationName, final String message, final Map<String, String> propertyMap) {
        final String correlationId = getCorrelationId();
        jmsTemplate.send(destinationName, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setJMSCorrelationID(correlationId);
            if (propertyMap != null && !propertyMap.isEmpty()) {
                for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
                    textMessage.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            return textMessage;
        });
        return correlationId;
    }

    public String receive(String destinationName) {
        Message receiveMessage = jmsTemplate.receive(destinationName);
        return resolveTextMessage(receiveMessage);
    }

    public String receiveByCorrelationId(String destinationName, String correlationId) {
        Message receiveMessage = jmsTemplate.receiveSelected(destinationName, "JMSCorrelationID = '"
                + correlationId + "'");
        return resolveTextMessage(receiveMessage);
    }

    public String receiveSelected(String destinationName, String messageSelector) {
        Message receiveMessage = jmsTemplate.receiveSelected(destinationName, messageSelector);
        return resolveTextMessage(receiveMessage);
    }

    @SneakyThrows
    private String resolveTextMessage(Message receiveMessage) {
        if (receiveMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) receiveMessage;
            return textMessage.getText();
        }
        return null;
    }

    private String getCorrelationId() {
        return UUID.randomUUID().toString();
    }
}

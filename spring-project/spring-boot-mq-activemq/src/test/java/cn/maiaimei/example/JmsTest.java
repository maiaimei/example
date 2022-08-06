package cn.maiaimei.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;
import java.io.IOException;

public class JmsTest extends TestBase {
    private static final String ACTIVEMQ_URL = "tcp://192.168.1.19:61616";

    @Test
    public void testSend() throws JMSException {
        // 创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 创建并打开连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 创建会话
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Queue queue = session.createQueue(DEFAULT_QUEUE_NAME);
        // 创建生产者
        MessageProducer producer = session.createProducer(queue);
        // 创建消息
        TextMessage message = session.createTextMessage(DEFAULT_MESSAGE);
        // 发送消息
        producer.send(queue, message);
        // 释放资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceive1() throws JMSException {
        // 创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 创建并打开连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 创建会话
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Queue queue = session.createQueue(DEFAULT_QUEUE_NAME);
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        // 消费消息
        consumer.receive();
        // 释放资源
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceive2() throws JMSException, IOException {
        // 创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 创建并打开连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 创建会话
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Queue queue = session.createQueue(DEFAULT_QUEUE_NAME);
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        // 监听消息并消费
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("监听消息并消费消息：" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 给消费者一点时间，让它有机会处理消息
        System.in.read();
        // 释放资源
        consumer.close();
        session.close();
        connection.close();
    }
}

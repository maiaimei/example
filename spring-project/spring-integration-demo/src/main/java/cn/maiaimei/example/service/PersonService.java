package cn.maiaimei.example.service;

import cn.maiaimei.example.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.Random;

@Slf4j
public class PersonService {
    private static final String HTTP_STATUS_CODE = "http_statusCode";
    private static final Random RANDOM = new Random();

    public Message<?> create(Message<User> msg) {
        User user = msg.getPayload();
        log.info("create person, user is {}", user);
        user.setId(Math.abs(RANDOM.nextLong()));
        return MessageBuilder.withPayload(user)
                .copyHeadersIfAbsent(msg.getHeaders())
                .setHeader(HTTP_STATUS_CODE, HttpStatus.OK)
                .build();
    }

    public Message<?> get(Message<String> msg) {
        long id = Long.parseLong(msg.getPayload());
        log.info("get person, id is {}", id);
        int count = RANDOM.nextInt(5) + 3;
        String name = RandomStringUtils.randomAlphabetic(count);
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        User user = new User();
        user.setId(id);
        user.setName(name);
        return MessageBuilder.withPayload(user)
                .copyHeadersIfAbsent(msg.getHeaders())
                .setHeader(HTTP_STATUS_CODE, HttpStatus.OK)
                .build();
    }

    public void delete(Message<String> msg) {
        long id = Long.parseLong(msg.getPayload());
        log.info("delete person, id is {}", id);
    }
}

package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProducerService {

    public String produce1() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 1 produce message: {}", payload);
        return payload;
    }

    public String produce2() {
        final String payload = UUID.randomUUID().toString();
        log.info("===> producer 2 produce message: {}", payload);
        return payload;
    }

}

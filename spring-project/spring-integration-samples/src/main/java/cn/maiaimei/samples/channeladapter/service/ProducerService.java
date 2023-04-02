package cn.maiaimei.samples.channeladapter.service;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
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

package cn.maiaimei.example.controller;

import cn.maiaimei.example.event.MyApplicationEvent;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "事件发布与监听")
@Slf4j
@RestController
@RequestMapping("/event")
public class EventController {
    private ApplicationContext applicationContext;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public EventController(ApplicationContext applicationContext,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.applicationContext = applicationContext;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/publish/1")
    public void publish1(@RequestParam String message) {
        log.info("发布事件 ===> {}", message);
        applicationContext.publishEvent(new MyApplicationEvent(message));
    }

    @GetMapping("/publish/2")
    public void publish2(@RequestParam String message) {
        log.info("发布事件 ===> {}", message);
        applicationEventPublisher.publishEvent(new MyApplicationEvent(message));
    }
}

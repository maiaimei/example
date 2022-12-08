package cn.maiaimei.example.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
public class MyApplicationEventListener1 {
    @EventListener
    public void handleEvent(MyApplicationEvent event) {
        log.info("处理事件 <=== {}", event);
    }
}

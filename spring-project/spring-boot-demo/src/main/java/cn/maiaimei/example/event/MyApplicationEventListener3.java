package cn.maiaimei.example.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(3)
public class MyApplicationEventListener3 implements ApplicationListener<MyApplicationEvent> {
    @Override
    public void onApplicationEvent(MyApplicationEvent event) {
        log.info("处理事件 <=== {}", event);
    }
}

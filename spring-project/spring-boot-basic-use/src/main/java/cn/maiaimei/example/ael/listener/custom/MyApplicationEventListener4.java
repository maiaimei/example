package cn.maiaimei.example.ael.listener.custom;

import cn.maiaimei.example.ael.event.MyApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(2)
public class MyApplicationEventListener4 implements ApplicationListener<MyApplicationEvent> {
    @Override
    public void onApplicationEvent(MyApplicationEvent event) {
        log.info("处理事件 <=== {}", event);
    }
}

package cn.maiaimei.example.ael.listener.custom;

import cn.maiaimei.example.ael.event.MyApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyApplicationEventListener1 {
    @EventListener
    public void handleEvent(MyApplicationEvent event) {
        log.info("处理事件 <=== {}", event);
    }
}

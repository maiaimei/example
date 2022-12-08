package cn.maiaimei.example.boot.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class MyContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("===== Application Events and Listeners ===== A ContextRefreshedEvent is sent when an ApplicationContext is refreshed");
    }
}

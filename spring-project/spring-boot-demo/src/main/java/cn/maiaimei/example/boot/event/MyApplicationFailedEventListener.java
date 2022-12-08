package cn.maiaimei.example.boot.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationFailedEvent is sent if there is an exception on startup");
    }
}

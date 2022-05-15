package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationStartedEvent is sent after the context has been refreshed but before any application and command-line runners have been called");
    }
}

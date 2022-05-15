package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationReadyEvent is sent after any application and command-line runners have been called");
    }
}

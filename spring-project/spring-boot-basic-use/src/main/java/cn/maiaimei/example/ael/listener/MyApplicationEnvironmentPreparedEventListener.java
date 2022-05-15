package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationEnvironmentPreparedEvent is sent when the Environment to be used in the context is known but before the context is created");
    }
}

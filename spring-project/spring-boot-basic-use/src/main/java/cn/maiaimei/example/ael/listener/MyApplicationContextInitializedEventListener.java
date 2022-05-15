package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationContextInitializedEventListener implements ApplicationListener<ApplicationContextInitializedEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationContextInitializedEvent is sent when the ApplicationContext is prepared and ApplicationContextInitializers have been called but before any bean definitions are loaded");
    }
}

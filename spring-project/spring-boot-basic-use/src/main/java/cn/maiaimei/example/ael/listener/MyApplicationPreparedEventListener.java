package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        log.info("===== Application Events and Listeners ===== An ApplicationPreparedEvent is sent just before the refresh is started but after bean definitions have been loaded");
    }
}

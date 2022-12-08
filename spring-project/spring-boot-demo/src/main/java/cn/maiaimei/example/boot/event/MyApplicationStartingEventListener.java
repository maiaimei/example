package cn.maiaimei.example.boot.event;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

public class MyApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("===== Application Events and Listeners ===== An ApplicationStartingEvent is sent at the start of a run but before any processing, except for the registration of listeners and initializers");
    }
}

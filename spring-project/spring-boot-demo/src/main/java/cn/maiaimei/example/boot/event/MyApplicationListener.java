package cn.maiaimei.example.boot.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class MyApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("===== Application Events and Listeners ===== " + event.getClass().getSimpleName());
    }
}

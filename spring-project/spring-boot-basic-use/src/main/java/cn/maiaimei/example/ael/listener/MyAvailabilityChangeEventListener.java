package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyAvailabilityChangeEventListener implements ApplicationListener<AvailabilityChangeEvent> {
    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        log.info("===== Application Events and Listeners ===== An AvailabilityChangeEvent is sent right after with LivenessState.CORRECT to indicate that the application is considered as live");
    }
}

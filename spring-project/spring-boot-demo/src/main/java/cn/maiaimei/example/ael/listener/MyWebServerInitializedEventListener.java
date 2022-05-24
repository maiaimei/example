package cn.maiaimei.example.ael.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class MyWebServerInitializedEventListener implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("===== Application Events and Listeners ===== A WebServerInitializedEvent is sent after the WebServer is ready. ServletWebServerInitializedEvent and ReactiveWebServerInitializedEvent are the servlet and reactive variants respectively");
    }
}

package cn.maiaimei.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class XxxApplicationListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(XxxApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("收到事件：{}", event);
    }
}

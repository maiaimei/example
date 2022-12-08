package cn.maiaimei.example.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

@Slf4j
public class MySpringApplicationRunListener implements SpringApplicationRunListener {
    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
        // do nothing
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.starting");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.contextLoaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.started");
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.ready");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("===== 运行监听器 RunListener ===== MySpringApplicationRunListener.failed");
    }
}

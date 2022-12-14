package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("ServletContextListener.contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("ServletContextListener.contextDestroyed");
    }
}

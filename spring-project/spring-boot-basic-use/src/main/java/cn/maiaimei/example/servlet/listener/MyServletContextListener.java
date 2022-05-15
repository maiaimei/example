package cn.maiaimei.example.servlet.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class MyServletContextListener implements ServletContextListener, ServletContextAttributeListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("===== Servlet规范之监听器 ===== ServletContextListener.contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("===== Servlet规范之监听器 ===== ServletContextListener.contextDestroyed");
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        log.info("===== Servlet规范之监听器 ===== ServletContextAttributeListener.attributeAdded, name:{}, value:{}", scae.getName(), scae.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        log.info("===== Servlet规范之监听器 ===== ServletContextAttributeListener.attributeRemoved, name:{}, value:{}", scae.getName(), scae.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        log.info("===== Servlet规范之监听器 ===== ServletContextAttributeListener.attributeReplaced, name:{}, value:{}", scae.getName(), scae.getValue());
    }
}

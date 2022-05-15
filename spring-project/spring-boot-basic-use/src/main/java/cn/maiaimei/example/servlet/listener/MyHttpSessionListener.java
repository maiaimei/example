package cn.maiaimei.example.servlet.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@WebListener
public class MyHttpSessionListener implements HttpSessionListener, HttpSessionAttributeListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("===== Servlet规范之监听器 ===== HttpSessionListener.sessionCreated");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("===== Servlet规范之监听器 ===== HttpSessionListener.sessionDestroyed");
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        log.info("===== Servlet规范之监听器 ===== HttpSessionAttributeListener.attributeAdded, name:{}, value:{}", se.getName(), se.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        log.info("===== Servlet规范之监听器 ===== HttpSessionAttributeListener.attributeRemoved, name:{}, value:{}", se.getName(), se.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        log.info("===== Servlet规范之监听器 ===== HttpSessionAttributeListener.attributeReplaced, name:{}, value:{}", se.getName(), se.getValue());
    }
}

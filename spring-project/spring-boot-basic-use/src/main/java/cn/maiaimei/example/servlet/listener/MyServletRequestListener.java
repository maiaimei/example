package cn.maiaimei.example.servlet.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class MyServletRequestListener implements ServletRequestListener, ServletRequestAttributeListener {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        log.info("===== Servlet规范之监听器 ===== ServletRequestListener.requestInitialized");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        log.info("===== Servlet规范之监听器 ===== ServletRequestListener.requestDestroyed");
    }

    @SneakyThrows
    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        log.info("===== Servlet规范之监听器 ===== ServletRequestAttributeListener.attributeAdded, name:{}, value:{}", srae.getName(), srae.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        log.info("===== Servlet规范之监听器 ===== ServletRequestAttributeListener.attributeRemoved, name:{}, value:{}", srae.getName(), srae.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        log.info("===== Servlet规范之监听器 ===== ServletRequestAttributeListener.attributeReplaced, name:{}, value:{}", srae.getName(), srae.getValue());
    }
}

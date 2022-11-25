package cn.maiaimei.example.component;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * {@link TaskDecorator} 用于处理主线程与子线程之间的数据传递，比如：日志TracerId
 */
public class CustomTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // 把主线程的context制作一份副本
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (copyOfContextMap != null) {
                    // 放到MDC中去子线程的contextMap中去
                    MDC.setContextMap(copyOfContextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}

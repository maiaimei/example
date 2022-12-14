package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;

/**
 * Tomcat服务器启动的时候会扫描META-INF/services/javax.servlet.ServletContainerInitializer文件的类（实现ServletContainerInitializer接口）
 * 并调用onStartup方法。
 * <p>
 * 容器启动的时候会将@HandlesTypes指定类型的子类、实现类传递到Set<Class<?>> set中
 */
@HandlesTypes(value = {UserService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {
    private static final Logger log = LoggerFactory.getLogger(MyServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        log.info("ServletContainerInitializer.onStartup start");
        if (set != null && !set.isEmpty()) {
            for (Class<?> clazz : set) {
                log.info("{}", clazz.getName());
            }
        } else {
            log.info("set is empty");
        }
        // 注册Servlet
        ServletRegistration.Dynamic myHttpServlet = servletContext.addServlet("MyHttpServlet", MyHttpServlet.class);
        myHttpServlet.addMapping("/my-http-servlet");
        myHttpServlet.setLoadOnStartup(1);
        // 注册Filter
        FilterRegistration.Dynamic myFilter = servletContext.addFilter("MyFilter", MyFilter.class);
        myFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), Boolean.TRUE, "/*");
        // 注册Listener
        servletContext.addListener(MyListener.class);
        log.info("ServletContainerInitializer.onStartup end");
    }
}

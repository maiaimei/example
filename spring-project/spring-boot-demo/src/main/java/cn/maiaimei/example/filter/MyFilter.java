package cn.maiaimei.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * 过滤器注册见 {@link cn.maiaimei.example.config.FilterConfig}
 */
public class MyFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            log.info("===== Servlet规范之过滤器 ===== Before Filter.doFilter");
            chain.doFilter(request, response);
        } catch (Exception ex) {
            log.info("===== Servlet规范之过滤器 ===== AfterThrowing Filter.doFilter");
        } finally {
            log.info("===== Servlet规范之过滤器 ===== After Filter.doFilter");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("===== Servlet规范之过滤器 ===== Filter.init");
    }

    @Override
    public void destroy() {
        log.info("===== Servlet规范之过滤器 ===== Filter.destroy");
    }
}

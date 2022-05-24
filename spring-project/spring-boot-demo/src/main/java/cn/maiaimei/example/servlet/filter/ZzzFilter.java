package cn.maiaimei.example.servlet.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class ZzzFilter implements Filter {
    //@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("===== Servlet规范之过滤器 ===== ZzzFilter.init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("===== Servlet规范之过滤器 ===== Before ZzzFilter.doFilter");
        chain.doFilter(request, response);
        log.info("===== Servlet规范之过滤器 ===== After ZzzFilter.doFilter");
    }

    @Override
    public void destroy() {
        log.info("===== Servlet规范之过滤器 ===== ZzzFilter.destroy");
    }
}

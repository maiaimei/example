package cn.maiaimei.example.servlet.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class YyyFilter implements Filter {
    //@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("===== Servlet规范之过滤器 ===== YyyFilter.init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("===== Servlet规范之过滤器 ===== Before YyyFilter.doFilter");
        chain.doFilter(request, response);
        log.info("===== Servlet规范之过滤器 ===== After YyyFilter.doFilter");
    }

    @Override
    public void destroy() {
        log.info("===== Servlet规范之过滤器 ===== YyyFilter.destroy");
    }
}

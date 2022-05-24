package cn.maiaimei.example.servlet.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class XxxFilter implements Filter {
    //@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("===== Servlet规范之过滤器 ===== XxxFilter.init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("===== Servlet规范之过滤器 ===== Before XxxFilter.doFilter");
        chain.doFilter(request, response);
        log.info("===== Servlet规范之过滤器 ===== After XxxFilter.doFilter");
    }

    @Override
    public void destroy() {
        log.info("===== Servlet规范之过滤器 ===== XxxFilter.destroy");
    }
}

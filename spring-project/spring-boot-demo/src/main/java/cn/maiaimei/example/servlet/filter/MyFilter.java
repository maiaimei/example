package cn.maiaimei.example.servlet.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @WebFilter 无法保证过滤器之间的执行顺序
 */
@Slf4j
@WebFilter(filterName = "myFilter", urlPatterns = "/*")
public class MyFilter implements Filter {
    //@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("===== Servlet规范之过滤器 ===== MyFilter.init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("===== Servlet规范之过滤器 ===== Before MyFilter.doFilter");
        chain.doFilter(request, response);
        log.info("===== Servlet规范之过滤器 ===== After MyFilter.doFilter");
    }

    @Override
    public void destroy() {
        log.info("===== Servlet规范之过滤器 ===== MyFilter.destroy");
    }
}

package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Before MyFilter");
        if (servletRequest instanceof HttpServletRequest) {
            log.info("Request URI: {}", ((HttpServletRequest) servletRequest).getRequestURI());
        }
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("After MyFilter");
    }
}

package cn.maiaimei.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFiveFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    log.info("before doFilter Filter接口 + FilterRegistrationBean");
    filterChain.doFilter(servletRequest, servletResponse);
    log.info("after doFilter Filter接口 + FilterRegistrationBean");
  }
}

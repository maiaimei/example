package cn.maiaimei.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(3)
@WebFilter(filterName = "customThreeFilter", urlPatterns = "/*")
public class CustomThreeFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    log.info("before doFilter Filter接口 + @WebFilter注解");
    filterChain.doFilter(servletRequest, servletResponse);
    log.info("after doFilter Filter接口 + @WebFilter注解");
  }
}

package cn.maiaimei.example.filter;

import static cn.maiaimei.example.constant.Constants.TRACE_ID;

import cn.maiaimei.example.utils.TraceIdUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class AddTraceIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    //addTraceIdByContext(request, response, filterChain);
    addTraceIdByMDC(request, response, filterChain);
  }

  private void addTraceIdByContext(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      TraceIdUtils.setTraceId(TraceIdUtils.getTraceId(request));
      filterChain.doFilter(request, response);
    } finally {
      TraceIdUtils.removeTraceId();
    }
  }

  private void addTraceIdByMDC(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      MDC.put(TRACE_ID, TraceIdUtils.getTraceId(request));
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(TRACE_ID);
    }
  }
}

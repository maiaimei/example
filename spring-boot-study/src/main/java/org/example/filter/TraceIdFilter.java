package org.example.filter;

import static org.example.constants.GlobalConstants.REQUEST_HEADER_TRACE_ID;
import static org.example.constants.GlobalConstants.TRACE_ID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.utils.TraceIdUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      // 尝试从请求头获取traceId
      String traceId = getOrGenerateTraceId(request);

      // 设置traceId到ThreadLocal
      TraceIdUtils.setTraceId(traceId);

      // 设置响应头
      response.setHeader(REQUEST_HEADER_TRACE_ID, traceId);

      // 设置MDC用于日志
      MDC.put(TRACE_ID, traceId);

      // 继续处理请求
      filterChain.doFilter(request, response);

    } finally {
      // 清理现场
      MDC.remove(TRACE_ID);
      TraceIdUtils.clearTraceId();
    }
  }

  private String getOrGenerateTraceId(HttpServletRequest request) {
    // 优先从请求头获取
    String traceId = request.getHeader(REQUEST_HEADER_TRACE_ID);

    // 如果请求头中没有，则从请求参数获取
    if (!StringUtils.hasText(traceId)) {
      traceId = request.getParameter(TRACE_ID);
    }

    // 如果都没有，则生成新的traceId
    if (!StringUtils.hasText(traceId)) {
      traceId = TraceIdUtils.generateTraceId();
    }

    return traceId;
  }

}

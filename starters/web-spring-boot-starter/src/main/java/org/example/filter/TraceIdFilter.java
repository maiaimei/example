package org.example.filter;

import static org.example.constants.WebConstants.HEADER_X_TRACE_ID;
import static org.example.constants.WebConstants.TRACE_ID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.example.properties.CustomFilterProperties;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class TraceIdFilter extends OncePerRequestFilter {

  private final List<String> excludePaths;

  private final AntPathMatcher antPathMatcher;

  public TraceIdFilter(CustomFilterProperties customFilterProperties) {
    this.excludePaths = customFilterProperties.getExcludePaths();
    this.antPathMatcher = new AntPathMatcher();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // 检查当前路径是否匹配任何排除路径
    return excludePaths.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      // 尝试从请求头获取traceId
      String traceId = getOrCreateTraceId(request);

      // 设置traceId
      MDC.put(TRACE_ID, traceId);

      // 设置响应头
      response.setHeader(HEADER_X_TRACE_ID, traceId);

      // 继续处理请求
      filterChain.doFilter(request, response);

    } finally {
      // 清理traceId
      MDC.remove(TRACE_ID);
    }
  }

  private String getOrCreateTraceId(HttpServletRequest request) {
    // 优先从请求头获取
    String traceId = request.getHeader(HEADER_X_TRACE_ID);

    // 如果请求头中没有，则从请求参数获取
    if (!StringUtils.hasText(traceId)) {
      traceId = request.getParameter(TRACE_ID);
    }

    // 如果都没有，则生成新的traceId
    if (!StringUtils.hasText(traceId)) {
      traceId = UUID.randomUUID().toString();
    }

    return traceId;
  }

}

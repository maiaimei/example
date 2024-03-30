package cn.maiaimei.example.filter;

import static cn.maiaimei.example.constant.Constants.EMPTY_STRING;
import static cn.maiaimei.example.constant.Constants.LOG_FILE;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class AddLogFileFilter extends OncePerRequestFilter {

  private static final String HEALTH_CHECK_URI = "/health-check";
  private static final String SERVICE_CENTER_URI = "/service-center/*";
  private static final String HEART_BEAT_URI = "/service-center/heart-beat";

  // log file name
  private static final String HEALTH_CHECK_LOG_FILE = "-health-check";
  private static final String SERVICE_CENTER_LOG_FILE = "-service-center";
  private static final String HEART_BEAT_LOG_FILE = "-heart-beat";

  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Value("${spring.application.name:application}")
  private String appName;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      MDC.put(LOG_FILE, getLogFile(request));
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(LOG_FILE);
    }
  }

  private String getLogFile(HttpServletRequest request) {
    final String uri = request.getRequestURI();
    String suffix = EMPTY_STRING;
    if (uri.endsWith(HEALTH_CHECK_URI)) {
      suffix = HEALTH_CHECK_LOG_FILE;
    } else if (uri.endsWith(HEART_BEAT_URI)) {
      suffix = HEART_BEAT_LOG_FILE;
    } else if (antPathMatcher.match(SERVICE_CENTER_URI, uri)) {
      suffix = SERVICE_CENTER_LOG_FILE;
    }
    return appName.concat(suffix);
  }
}

package cn.maiaimei.example.interceptor;

import static cn.maiaimei.example.constant.Constants.TRACE_ID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class AddTraceIdInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    log.info("preHandle traceId: {}", MDC.get(TRACE_ID));
    //TraceIdUtils.setTraceId(TraceIdUtils.getTraceId(request));
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    log.info("postHandle traceId: {}", MDC.get(TRACE_ID));
    //TraceIdUtils.removeTraceId();
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }
}

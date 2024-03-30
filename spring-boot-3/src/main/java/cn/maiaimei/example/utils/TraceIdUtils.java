package cn.maiaimei.example.utils;

import static cn.maiaimei.example.constant.Constants.EMPTY_STRING;
import static cn.maiaimei.example.constant.Constants.HYPHEN;
import static cn.maiaimei.example.constant.Constants.TRACE_ID;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.util.StringUtils;

public class TraceIdUtils {

  private static final ThreadLocal<String> TRACE_ID_THREAD_LOCAL = new ThreadLocal<>();

  private TraceIdUtils() {
  }

  public static String getTraceId() {
    return TRACE_ID_THREAD_LOCAL.get();
  }

  public static void setTraceId(String traceId) {
    TRACE_ID_THREAD_LOCAL.set(traceId);
  }

  public static void removeTraceId() {
    TRACE_ID_THREAD_LOCAL.remove();
  }
  
  public static String getTraceId(HttpServletRequest request) {
    String traceId = request.getHeader(TRACE_ID);
    if (StringUtils.hasText(traceId)) {
      return traceId;
    }
    return UUID.randomUUID().toString().replaceAll(HYPHEN, EMPTY_STRING);
  }
}

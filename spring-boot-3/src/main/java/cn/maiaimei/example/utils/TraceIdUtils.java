package cn.maiaimei.example.utils;

import static cn.maiaimei.example.constant.Constants.EMPTY_STRING;
import static cn.maiaimei.example.constant.Constants.HYPHEN;
import static cn.maiaimei.example.constant.Constants.TRACE_ID;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class TraceIdUtils {

  private TraceIdUtils() {
  }

  public static String getTraceId() {
    return MDC.get(TRACE_ID);
  }

  public static void setTraceId(String traceId) {
    MDC.put(TRACE_ID, traceId);
  }

  public static void removeTraceId() {
    MDC.remove(TRACE_ID);
  }

  public static String getTraceId(HttpServletRequest request) {
    String traceId = request.getHeader(TRACE_ID);
    if (!StringUtils.hasText(traceId)) {
      traceId = UUID.randomUUID().toString().replaceAll(HYPHEN, EMPTY_STRING);
    }
    return traceId;
  }

  public static String getOrSetTraceId() {
    String traceId = MDC.get(TRACE_ID);
    if (!StringUtils.hasText(traceId)) {
      traceId = UUID.randomUUID().toString().replaceAll(HYPHEN, EMPTY_STRING);
      setTraceId(traceId);
    }
    return traceId;
  }
}

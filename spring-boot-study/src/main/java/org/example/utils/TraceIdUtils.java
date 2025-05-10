package org.example.utils;

public class TraceIdUtils {

  private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

  // 生成traceId
  public static String generateTraceId() {
    return IdGenerator.nextId().toPlainString();
  }

  // 设置traceId
  public static void setTraceId(String traceId) {
    TRACE_ID.set(traceId);
  }

  // 获取traceId
  public static String getTraceId() {
    return TRACE_ID.get();
  }

  // 清除traceId
  public static void clearTraceId() {
    TRACE_ID.remove();
  }
}

package cn.maiaimei.example.utils;

public class TraceIdContext {

  private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

  private TraceIdContext() {
  }

  public static void setTraceId(String traceId) {
    TRACE_ID.set(traceId);
  }

  public static void getTraceId() {
    TRACE_ID.get();
  }

  public static void removeTraceId() {
    TRACE_ID.remove();
  }
}

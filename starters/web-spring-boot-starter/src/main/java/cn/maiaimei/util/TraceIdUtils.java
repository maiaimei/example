package cn.maiaimei.util;

import static cn.maiaimei.constants.WebConstants.TRACE_ID;

import org.slf4j.MDC;

public class TraceIdUtils {

  // 获取traceId
  public static String getTraceId() {
    return MDC.get(TRACE_ID);
  }
}

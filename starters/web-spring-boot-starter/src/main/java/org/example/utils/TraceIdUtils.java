package org.example.utils;

import static org.example.constants.WebConstants.TRACE_ID;

import org.slf4j.MDC;

public class TraceIdUtils {

  // 获取traceId
  public static String getTraceId() {
    return MDC.get(TRACE_ID);
  }
}

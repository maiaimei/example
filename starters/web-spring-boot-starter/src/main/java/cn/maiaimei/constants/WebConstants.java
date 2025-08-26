package cn.maiaimei.constants;

public class WebConstants {

  public static final String TRACE_ID = "traceId";

  public static final String HEADER_X_TRACE_ID = "X-Trace-Id";
  public static final String HEADER_X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

  // 单层路径匹配模式，只匹配一级路径
  public static final String SINGLE_LEVEL_PATH_PATTERN = "/*";
  // 多层路径匹配模式，匹配所有层级的路径
  public static final String MULTI_LEVEL_PATH_PATTERN = "/**";
}

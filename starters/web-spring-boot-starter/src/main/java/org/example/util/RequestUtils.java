package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;

public class RequestUtils {

  public static String getUnifiedPath(Object request) {
    if (request instanceof HttpServletRequest httpServletRequest) {
      // 获取请求的 URI 部分（不包含主机名、端口号等）
      return httpServletRequest.getRequestURI();
    } else if (request instanceof ServerHttpRequest serverHttpRequest) {
      // 使用 getURI() 获取路径部分
      return serverHttpRequest.getURI().getPath();
    } else {
      throw new IllegalArgumentException("Unsupported request type");
    }
  }
}

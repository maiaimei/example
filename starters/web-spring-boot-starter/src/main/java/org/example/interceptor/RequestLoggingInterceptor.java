package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
//@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 记录基本请求信息
    log.info("Request Path: {} {}", request.getMethod(), request.getRequestURI());
    log.info("Content Type: {}", request.getContentType());

    // 记录请求头
    Collections.list(request.getHeaderNames()).forEach(headerName -> {
      log.info("Header {}: {}", headerName, request.getHeader(headerName));
    });

    // 记录查询参数
    String queryString = request.getQueryString();
    if (queryString != null) {
      log.info("Query String: {}", queryString);
    }

    // 记录请求体 (需要特殊处理，因为InputStream只能读取一次)
    if (request.getContentType() != null && request.getContentType().contains("application/json")) {
      String requestBody = getRequestBody(request);
      log.info("Request Body: {}", requestBody);
    }

    // 处理 Multipart 请求
    if (request instanceof MultipartHttpServletRequest multipartHttpServletRequest) {
      logMultipartRequest(multipartHttpServletRequest);
    }

    return true; // 返回true继续执行，返回false中断请求
  }

  private String getRequestBody(HttpServletRequest request) throws IOException {
    // 包装HttpServletRequest以允许多次读取
    ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
    return new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
  }

  private void logMultipartRequest(MultipartHttpServletRequest multipartHttpServletRequest) {
    try {
      multipartHttpServletRequest.getFileMap().forEach((paramName, file) -> {
        log.info("File upload - name: {}, original filename: {}, size: {}",
            paramName,
            file.getOriginalFilename(),
            file.getSize());
      });
    } catch (Exception e) {
      log.error("Error logging multipart request", e);
    }
  }
}

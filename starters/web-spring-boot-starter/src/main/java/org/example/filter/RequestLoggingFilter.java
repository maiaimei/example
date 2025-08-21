package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  public RequestLoggingFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

    try {
      // 在请求处理前记录基本信息
      logRequestStart(requestWrapper);

      filterChain.doFilter(requestWrapper, response);
    } finally {
      // 在请求处理后记录详细信息
      logRequestDetails(requestWrapper);
    }
  }

  private void logRequestStart(HttpServletRequest request) {
    log.info("Started {} request to {}",
        request.getMethod(),
        request.getRequestURI() + (StringUtils.hasText(request.getQueryString()) ? "?" + request.getQueryString() : "")
    );
  }

  private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException, ServletException {
    Map<String, Object> requestInfo = new HashMap<>();

    // 基本请求信息
    requestInfo.put("method", request.getMethod());
    requestInfo.put("path", request.getRequestURI());
    requestInfo.put("queryString", request.getQueryString());
    requestInfo.put("contentType", request.getContentType());

    // 请求头信息
    Map<String, String> headers = Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            request::getHeader,
            (v1, v2) -> v1
        ));
    requestInfo.put("headers", headers);

    // 请求参数
    Map<String, String[]> parameters = request.getParameterMap();
    requestInfo.put("parameters", parameters);

    // 请求体
    String contentType = request.getContentType();
    if (contentType != null) {
      if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
          String bodyContent = new String(content, StandardCharsets.UTF_8);
          requestInfo.put("body", bodyContent);
        }
      } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
        // Multipart 请求处理
        Map<String, Object> multipartInfo = new HashMap<>();
        Collection<Part> parts = request.getParts();

        for (Part part : parts) {
          Map<String, Object> partInfo = new HashMap<>();
          partInfo.put("name", part.getName());
          partInfo.put("contentType", part.getContentType());
          partInfo.put("size", part.getSize());

          if (part.getSubmittedFileName() != null) {
            // 文件类型的 part
            partInfo.put("filename", part.getSubmittedFileName());
          } else {
            // 非文件类型的 part，读取内容
            String value = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            partInfo.put("value", value);
          }

          multipartInfo.put(part.getName(), partInfo);
        }
        requestInfo.put("multipart", multipartInfo);
      }
    }

    // 将请求信息转换为JSON格式并记录
    try {
      String requestLog = objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(requestInfo);
      log.info("Request details: {}", requestLog);
    } catch (Exception e) {
      log.error("Error logging request details", e);
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // 可以在这里添加不需要记录日志的路径
    String path = request.getRequestURI();
    return path.contains("/actuator/") ||
        path.contains("/swagger-ui/") ||
        path.contains("/v3/api-docs");
  }
}


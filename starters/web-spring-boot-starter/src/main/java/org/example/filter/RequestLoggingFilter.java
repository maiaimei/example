package org.example.filter;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.example.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

    try {
      // 1. 记录请求开始的基本信息
      logRequestStart(requestWrapper);

      // 2. 先执行 filter chain，这样请求体才会被读取和缓存
      filterChain.doFilter(requestWrapper, response);
    } finally {
      // 3. 在 filter chain 执行后记录详细信息，此时才能获取到请求体
      logRequestDetails(requestWrapper);
    }
  }

  private void logRequestStart(HttpServletRequest request) {
    String queryString = Optional.ofNullable(request.getQueryString()).map(q -> "?" + q).orElse("");
    log.info("Started {} request to {}{}", request.getMethod(), request.getRequestURI(), queryString);
  }

  private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException, ServletException {
    Map<String, Object> requestInfo = new HashMap<>();

    // 基本请求信息
    requestInfo.put("method", request.getMethod());
    requestInfo.put("path", request.getRequestURI());
    requestInfo.put("queryString", request.getQueryString());
    requestInfo.put("contentType", request.getContentType());

    // 请求头信息
    requestInfo.put("headers", getRequestHeaders(request));

    // 请求参数
    requestInfo.put("parameters", request.getParameterMap());

    // 请求体
    String contentType = request.getContentType();
    if (contentType != null) {
      if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        requestInfo.put("body", getRequestBody(request));
      } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
        // Multipart 请求处理
        requestInfo.put("multipart", getMultipartInfo(request));
      }
    }

    // 将请求信息转换为JSON格式并记录
    try {
      String requestLog = JsonUtils.toJson(requestInfo);
      log.info("Request details: {}", requestLog);
    } catch (Exception e) {
      log.error("Error logging request details for URI: {}", request.getRequestURI(), e);
    }
  }

  private Map<String, String> getRequestHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            request::getHeader,
            (v1, v2) -> v1
        ));
  }

  private Map<String, Object> getMultipartInfo(HttpServletRequest request) throws IOException, ServletException {
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
    return multipartInfo;
  }

  private Map<String, Object> getRequestBody(ContentCachingRequestWrapper request) {
    try {
      byte[] content = request.getContentAsByteArray();
      if (content.length > 0) {
        String bodyContent = new String(content, StandardCharsets.UTF_8);
        return JsonUtils.toObject(bodyContent, new TypeReference<>() {
        });
      }
    } catch (Exception e) {
      log.error("Error parsing request body for URI: {}", request.getRequestURI(), e);
    }
    return Collections.emptyMap();
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


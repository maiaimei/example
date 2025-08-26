package org.example.http.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.http.CustomContentCachingRequestWrapper;
import org.example.properties.CustomFilterProperties;
import org.example.properties.RequestLoggingProperties;
import org.example.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final List<String> excludePaths;
  private final long slowRequestThresholdMs;
  private final AntPathMatcher antPathMatcher;

  public RequestLoggingFilter(CustomFilterProperties customFilterProperties, RequestLoggingProperties requestLoggingProperties) {
    this.excludePaths = getExcludePaths(customFilterProperties, requestLoggingProperties);
    this.slowRequestThresholdMs = requestLoggingProperties.getSlowRequestThresholdMs();
    this.antPathMatcher = new AntPathMatcher();
  }

  private List<String> getExcludePaths(CustomFilterProperties customFilterProperties,
      RequestLoggingProperties requestLoggingProperties) {
    final List<String> paths = Optional.ofNullable(requestLoggingProperties.getExcludePaths()).orElseGet(ArrayList::new);
    if ("Y".equalsIgnoreCase(requestLoggingProperties.getUseCustomFilterExcludePaths())) {
      paths.addAll(Optional.ofNullable(customFilterProperties.getExcludePaths()).orElseGet(ArrayList::new));
    }
    return paths.stream().distinct().toList();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // 检查当前路径是否匹配任何排除路径
    return excludePaths.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    CustomContentCachingRequestWrapper requestWrapper = new CustomContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    // 记录开始时间
    long startTime = System.currentTimeMillis();

    try {
      // 记录请求开始的基本信息
      log.info("Starting {} request to {}", request.getMethod(), request.getRequestURI());

      // 记录请求信息
      log.info("Request details: {}", JsonUtils.toJson(collectRequestDetails(requestWrapper)));

      filterChain.doFilter(requestWrapper, responseWrapper);
    } finally {
      // 记录响应信息
      log.info("Response details: {}", JsonUtils.toJson(collectResponseDetails(responseWrapper)));

      // 必须复制响应内容到原始响应对象
      responseWrapper.copyBodyToResponse();

      long duration = System.currentTimeMillis() - startTime;

      // 记录执行时间
      log.info("Completed {} request to {} - Status: {}, Execution time: {} ms",
          request.getMethod(), request.getRequestURI(), response.getStatus(), duration);

      // 对于较长时间的请求，可以添加警告日志
      if (duration > slowRequestThresholdMs) { // 5秒阈值可配置
        log.warn("Slow request detected! Execution time: {} ms - {} {}",
            duration, request.getMethod(), request.getRequestURI());
      }

      requestWrapper.cleanup();
    }
  }

  private Map<String, Object> collectRequestDetails(CustomContentCachingRequestWrapper request)
      throws IOException, ServletException {
    CustomHashMap<String, Object> requestInfo = new CustomHashMap<>();
    requestInfo.put("path", request.getRequestURI());
    requestInfo.put("method", request.getMethod());
    requestInfo.putIfNotEmpty("headers", getRequestHeaders(request));
    requestInfo.putIfHasText("contentType", request.getContentType());

    // 仅包含查询字符串部分（URL ? 后的部分），不包括表单参数。
    requestInfo.putIfHasText("queryString", request.getQueryString());

    // 包括查询字符串中的参数（URL ? 后的部分）和表单提交的参数（application/x-www-form-urlencoded 或 multipart/form-data）
    requestInfo.putIfNotEmpty("parameters", request.getParameterMap());

    String contentType = request.getContentType();
    if (Objects.nonNull(contentType)) {
      if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        requestInfo.putIfNotEmpty("body", getRequestBody(request));
      } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
        requestInfo.putIfNotEmpty("multipart", getMultipartInfo(request));
      } else {
        log.error("Unsupported content type: {}", contentType);
      }
    }
    return requestInfo;
  }

  private Map<String, Object> collectResponseDetails(ContentCachingResponseWrapper response) {
    CustomHashMap<String, Object> responseInfo = new CustomHashMap<>();
    responseInfo.put("status", response.getStatus());
    responseInfo.putIfNotEmpty("headers", getResponseHeaders(response));
    responseInfo.putIfHasText("contentType", response.getContentType());

    if (shouldLogResponseBody(response.getContentType())) {
      responseInfo.putIfNotNull("body", getResponseBody(response));
    } else {
      responseInfo.put("body", "[BINARY CONTENT OR FILE - NOT LOGGED]");
    }
    return responseInfo;
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

  private Map<String, String> getResponseHeaders(HttpServletResponse response) {
    return response.getHeaderNames()
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            response::getHeader,
            (v1, v2) -> v1
        ));
  }

  private Map<String, Object> getRequestBody(CustomContentCachingRequestWrapper request) {
    try {
      byte[] content = request.getContentAsByteArray();
      if (Objects.nonNull(content) && content.length > 0) {
        String contentValue = new String(content, request.getCharacterEncoding());
        return JsonUtils.toObject(contentValue, new TypeReference<>() {
        });
      }
    } catch (Exception e) {
      log.error("Error parsing request body", e);
    }
    return null;
  }

  private Map<String, Object> getMultipartInfo(HttpServletRequest request) throws IOException, ServletException {
    Map<String, Object> multipartInfo = new HashMap<>();
    Collection<Part> parts = request.getParts();

    for (Part part : parts) {
      CustomHashMap<String, Object> partInfo = new CustomHashMap<>();
      partInfo.put("name", part.getName());

      if (Objects.nonNull(part.getSubmittedFileName())) {
        // 文件类型的 part
        partInfo.put("filename", part.getSubmittedFileName());
        partInfo.putIfHasText("contentType", part.getContentType());
        partInfo.put("size", part.getSize());
      } else {
        // 非文件类型的 part，读取内容
        String value = new String(part.getInputStream().readAllBytes(), request.getCharacterEncoding());
        partInfo.put("value", value);
      }

      multipartInfo.put(part.getName(), partInfo);
    }
    return multipartInfo;
  }

  private boolean shouldLogResponseBody(String contentType) {
    return StringUtils.hasText(contentType) && (
        contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
            contentType.contains(MediaType.APPLICATION_PROBLEM_JSON_VALUE) ||
            contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
            contentType.contains(MediaType.TEXT_PLAIN_VALUE) ||
            contentType.contains(MediaType.TEXT_XML_VALUE) ||
            contentType.contains(MediaType.TEXT_HTML_VALUE)
    );
  }

  private Object getResponseBody(ContentCachingResponseWrapper response) {
    try {
      byte[] content = response.getContentAsByteArray();
      if (content.length > 0) {
        String contentType = response.getContentType();
        String contentValue = new String(content, response.getCharacterEncoding());
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
          return JsonUtils.toObject(contentValue, new TypeReference<>() {
          });
        }
        return contentValue;
      }
    } catch (Exception e) {
      log.error("Error reading response body", e);
    }
    return null;
  }

  private static class CustomHashMap<K, V> extends HashMap<K, V> {

    public void putIfHasText(K key, V value) {
      if (value instanceof String str && StringUtils.hasText(str)) {
        super.put(key, value);
      }
    }

    public void putIfNotEmpty(K key, V value) {
      if (value instanceof Map map && !CollectionUtils.isEmpty(map)) {
        super.put(key, value);
      }
      if (value instanceof Collection collection && !CollectionUtils.isEmpty(collection)) {
        super.put(key, value);
      }
    }

    public void putIfNotNull(K key, V value) {
      if (Objects.nonNull(value)) {
        super.put(key, value);
      }
    }

  }

}


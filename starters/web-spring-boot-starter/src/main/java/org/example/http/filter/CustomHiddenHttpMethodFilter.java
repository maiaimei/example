package org.example.http.filter;

import static org.example.constants.WebConstants.HEADER_X_HTTP_METHOD_OVERRIDE;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.util.WebUtils;

public class CustomHiddenHttpMethodFilter extends HiddenHttpMethodFilter {

  // 定义允许的 HTTP 方法
  private static final List<String> ALLOWED_METHODS =
      List.of(HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name());

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    HttpServletRequest requestToUse = request;

    // 检查请求头是否有 X-HTTP-Method-Override，如果没有检查请求参数是否有 _method
    String methodOverride = Optional.ofNullable(request.getHeader(HEADER_X_HTTP_METHOD_OVERRIDE))
        .orElse(request.getParameter(HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM));
    if (HttpMethod.POST.name().equals(request.getMethod()) && Objects.nonNull(methodOverride)
        && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
      String method = methodOverride.toUpperCase(Locale.ENGLISH);
      if (ALLOWED_METHODS.contains(method)) {
        requestToUse = new HttpMethodRequestWrapper(request, method);
      }
    }

    filterChain.doFilter(requestToUse, response);
  }

  // 内部类：HTTP 方法请求包装器
  private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

    private final String method;

    public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
      super(request);
      this.method = method;
    }

    @Override
    public String getMethod() {
      return this.method;
    }
  }
}

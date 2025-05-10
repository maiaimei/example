package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  // 定义允许的 HTTP 方法
  private static final List<String> ALLOWED_METHODS =
      List.of(HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name());

  private static final String REQUEST_HEADER_X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

  /**
   * 配置 HiddenHttpMethodFilter 以支持请求头 X-HTTP-Method-Override 或参数 _method覆盖 POST 方法
   */
  @Bean
  public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
    FilterRegistrationBean<HiddenHttpMethodFilter> registration = new FilterRegistrationBean<>();
    HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain)
          throws ServletException, IOException {

        HttpServletRequest requestToUse = request;

        // 检查请求头是否有 X-HTTP-Method-Override，如果没有检查请求参数是否有 _method
        String methodOverride = Optional.ofNullable(request.getHeader(REQUEST_HEADER_X_HTTP_METHOD_OVERRIDE))
            .orElse(request.getParameter(HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM));
        if (HttpMethod.POST.name().equals(request.getMethod()) && Objects.nonNull(methodOverride)) {
          String method = methodOverride.toUpperCase(Locale.ENGLISH);
          if (ALLOWED_METHODS.contains(method)) {
            requestToUse = new HttpMethodRequestWrapper(request, method);
          }
        }

        filterChain.doFilter(requestToUse, response);
      }
    };
    registration.setFilter(filter);
    registration.addUrlPatterns("/*");
    registration.setOrder(1);
    return registration;
  }

  /**
   * 配置自定义 FormContentFilter 以支持 1. 处理 PUT、PATCH 和 DELETE 请求中的表单数据 2. 将 application/x-www-form-urlencoded 内容转换为请求参数
   * {@link WebMvcAutoConfiguration} 已经自动配置了 {@link FormContentFilter}, 只有在你需要特别定制 FormContentFilter 的行为时才使用不同的 bean
   * 名称配置{@link FormContentFilter}
   */
  //@Bean(name = "customFormContentFilter")
  public FilterRegistrationBean<FormContentFilter> formContentFilter() {
    FilterRegistrationBean<FormContentFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new FormContentFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(2);
    return registration;
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
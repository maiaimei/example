package org.example.config;

import static org.example.constants.WebConstants.ALL_PATHS;

import org.example.filter.CustomHiddenHttpMethodFilter;
import org.example.filter.RequestLoggingFilter;
import org.example.filter.TraceIdFilter;
import org.example.properties.CustomFilterProperties;
import org.example.properties.RequestLoggingProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableConfigurationProperties(value = {
    CustomFilterProperties.class,
    RequestLoggingProperties.class
})
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean(CustomFilterProperties customFilterProperties) {
    FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
    TraceIdFilter filter = new TraceIdFilter(customFilterProperties);
    registration.setFilter(filter);
    registration.addUrlPatterns(ALL_PATHS);
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }

  /**
   * 配置 HiddenHttpMethodFilter 以支持请求头 X-HTTP-Method-Override 或参数 _method覆盖 POST 方法
   */
  @Bean
  public FilterRegistrationBean<HiddenHttpMethodFilter> customHiddenHttpMethodFilterRegistrationBean() {
    FilterRegistrationBean<HiddenHttpMethodFilter> registration = new FilterRegistrationBean<>();
    CustomHiddenHttpMethodFilter filter = new CustomHiddenHttpMethodFilter();
    registration.setFilter(filter);
    registration.addUrlPatterns(ALL_PATHS);
    registration.setOrder(1);
    return registration;
  }

  /**
   * 配置自定义 FormContentFilter以支持
   * 1. 处理 PUT、PATCH 和 DELETE 请求中的表单数据
   * 2. 将 application/x-www-form-urlencoded 内容转换为请求参数
   * {@link WebMvcAutoConfiguration} 已经自动配置了 {@link FormContentFilter},
   * 只有在你需要特别定制 FormContentFilter 的行为时才使用不同的 bean名称配置{@link FormContentFilter}
   */
  //@Bean
  public FilterRegistrationBean<FormContentFilter> formContentFilterRegistrationBean() {
    FilterRegistrationBean<FormContentFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new FormContentFilter());
    registration.addUrlPatterns(ALL_PATHS);
    registration.setOrder(2);
    return registration;
  }

  @Bean
  public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistrationBean(
      CustomFilterProperties customFilterProperties,
      RequestLoggingProperties requestLoggingProperties) {
    FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new RequestLoggingFilter(customFilterProperties, requestLoggingProperties));
    registration.addUrlPatterns(ALL_PATHS);
    registration.setOrder(3);
    return registration;
  }

}
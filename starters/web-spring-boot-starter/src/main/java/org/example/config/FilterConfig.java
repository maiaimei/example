package org.example.config;

import static org.example.constants.WebConstants.SINGLE_LEVEL_PATH_PATTERN;

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
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.FormContentFilter;

@Configuration
@EnableConfigurationProperties(value = {
    CustomFilterProperties.class,
    RequestLoggingProperties.class
})
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<TraceIdFilter> traceIdFilter(CustomFilterProperties customFilterProperties) {
    FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>();
    TraceIdFilter filter = new TraceIdFilter(customFilterProperties);
    registrationBean.setFilter(filter);
    registrationBean.addUrlPatterns(SINGLE_LEVEL_PATH_PATTERN);
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registrationBean;
  }

  /**
   * 配置 HiddenHttpMethodFilter 以支持请求头 X-HTTP-Method-Override 或参数 _method覆盖 POST 方法
   */
  @Bean
  public FilterRegistrationBean<CustomHiddenHttpMethodFilter> customHiddenHttpMethodFilter() {
    FilterRegistrationBean<CustomHiddenHttpMethodFilter> registrationBean = new FilterRegistrationBean<>();
    CustomHiddenHttpMethodFilter filter = new CustomHiddenHttpMethodFilter();
    registrationBean.setFilter(filter);
    registrationBean.addUrlPatterns(SINGLE_LEVEL_PATH_PATTERN);
    registrationBean.setOrder(1);
    return registrationBean;
  }

  /**
   * 配置自定义 FormContentFilter以支持
   * 1. 处理 PUT、PATCH 和 DELETE 请求中的表单数据
   * 2. 将 application/x-www-form-urlencoded 内容转换为请求参数
   * {@link WebMvcAutoConfiguration} 已经自动配置了 {@link FormContentFilter},
   * 只有在你需要特别定制 FormContentFilter 的行为时才使用不同的 bean名称配置{@link FormContentFilter}
   */
  //@Bean
  public FilterRegistrationBean<FormContentFilter> formContentFilter() {
    FilterRegistrationBean<FormContentFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new FormContentFilter());
    registrationBean.addUrlPatterns(SINGLE_LEVEL_PATH_PATTERN);
    registrationBean.setOrder(2);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter(
      CustomFilterProperties customFilterProperties,
      RequestLoggingProperties requestLoggingProperties) {
    FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestLoggingFilter(customFilterProperties, requestLoggingProperties));
    registrationBean.addUrlPatterns(SINGLE_LEVEL_PATH_PATTERN);
    registrationBean.setOrder(3);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter() {
    FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
    CommonsRequestLoggingFilter requestLoggingFilter = new CommonsRequestLoggingFilter();
    requestLoggingFilter.setIncludeQueryString(true);
    requestLoggingFilter.setIncludePayload(true);
    requestLoggingFilter.setMaxPayloadLength(10000);
    registrationBean.setFilter(requestLoggingFilter);
    registrationBean.addUrlPatterns(SINGLE_LEVEL_PATH_PATTERN);
    registrationBean.setOrder(4);
    return registrationBean;
  }

}
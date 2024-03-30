package cn.maiaimei.example.config;

import cn.maiaimei.example.filter.CustomFiveFilter;
import cn.maiaimei.example.filter.CustomFourFilter;
import cn.maiaimei.example.interceptor.AddTraceIdInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public FilterRegistrationBean<Filter> customFourFilter() {
    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new CustomFourFilter());
    registration.addUrlPatterns("/*");
    registration.setName("customFourFilter");
    registration.setOrder(4);
    return registration;
  }

  @Bean
  public FilterRegistrationBean<Filter> customFiveFilter() {
    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new CustomFiveFilter());
    registration.addUrlPatterns("/*");
    registration.setName("customFiveFilter");
    registration.setOrder(5);
    return registration;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册拦截器
    registry.addInterceptor(new AddTraceIdInterceptor());
  }
}

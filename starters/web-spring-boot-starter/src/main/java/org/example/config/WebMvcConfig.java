package org.example.config;

import static org.example.constants.WebConstants.MULTI_LEVEL_PATH_PATTERN;

import org.example.interceptor.RequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private RequestLoggingInterceptor requestLoggingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestLoggingInterceptor)
        .addPathPatterns(MULTI_LEVEL_PATH_PATTERN); // 拦截所有请求
  }
}
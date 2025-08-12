package org.example.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(value = "spring.mvc.cors.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig {

  private final CorsProperties corsProperties;

  public CorsConfig(CorsProperties corsProperties) {
    this.corsProperties = corsProperties;
  }

  /**
   * 通过 `WebMvcConfigurer` 实现（Spring Boot 2.x 推荐）
   */
  //@Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        final CorsRegistration corsRegistration = registry.addMapping("/**");// 对所有接口都有效

        // 使用配置属性
        CorsProperties.Global global = corsProperties.getGlobal();

        // 设置允许的源
        if (global.getAllowedOrigins() != null) {
          corsRegistration.allowedOrigins(global.getAllowedOrigins().toArray(new String[0]));
        }

        // 设置允许的方法
        if (global.getAllowedMethods() != null) {
          corsRegistration.allowedMethods(global.getAllowedMethods().toArray(new String[0]));
        }

        // 设置允许的头
        if (global.getAllowedHeaders() != null) {
          corsRegistration.allowedHeaders(global.getAllowedHeaders().toArray(new String[0]));
        }

        // 设置是否允许凭证
        corsRegistration.allowCredentials(global.isAllowCredentials());

        // 设置预检请求的缓存时间
        corsRegistration.maxAge(global.getMaxAge());
      }
    };
  }

  /**
   * Spring Boot 3.x / Spring 6 推荐方式（基于 `CorsConfigurationSource`）
   */
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();

    // 使用配置属性
    CorsProperties.Global global = corsProperties.getGlobal();

    // 设置允许的源
    if (global.getAllowedOrigins() != null) {
      config.setAllowedOrigins(global.getAllowedOrigins());
    }

    // 设置允许的方法
    if (global.getAllowedMethods() != null) {
      config.setAllowedMethods(global.getAllowedMethods());
    }

    // 设置允许的头
    if (global.getAllowedHeaders() != null) {
      config.setAllowedHeaders(global.getAllowedHeaders());
    }

    // 设置允许的源模式
    if (global.getAllowedOriginPatterns() != null) {
      config.setAllowedOriginPatterns(global.getAllowedOriginPatterns());
    }

    // 设置是否允许凭证
    config.setAllowCredentials(global.isAllowCredentials());

    // 设置预检请求的缓存时间
    config.setMaxAge(global.getMaxAge());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

}
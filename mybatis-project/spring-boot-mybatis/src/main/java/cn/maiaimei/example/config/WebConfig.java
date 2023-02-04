package cn.maiaimei.example.config;

import cn.maiaimei.framework.web.servlet.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseLoggingFilter() {
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setExcludeUris(Arrays.asList("/swagger", "/v2/api-docs", "/h2-console"));

        FilterRegistrationBean<RequestResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }
}

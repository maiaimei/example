package cn.maiaimei.example.config;

import cn.maiaimei.framework.web.http.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean requestResponseLoggingFilter() {
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setIncludeUserAgent(true);
        requestResponseLoggingFilter.setIncludeClientIP(true);
        requestResponseLoggingFilter.setIncludeClientDevice(true);
        requestResponseLoggingFilter.setIncludeClientOS(true);
        requestResponseLoggingFilter.setIncludeHeaderNames(Arrays.asList("X-Auth-Token"));
        requestResponseLoggingFilter.setExcludeUris(Arrays.asList("/swagger"));
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        bean.addUrlPatterns("/*");
        return bean;
    }
}
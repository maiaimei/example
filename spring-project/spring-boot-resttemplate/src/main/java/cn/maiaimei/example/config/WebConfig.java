package cn.maiaimei.example.config;

import cn.maiaimei.framework.web.http.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {
    //@Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseLoggingFilter() {
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setIncludeUserAgent(true);
        requestResponseLoggingFilter.setIncludeClientIP(true);
        requestResponseLoggingFilter.setIncludeClientDevice(true);
        requestResponseLoggingFilter.setIncludeClientOS(true);
        FilterRegistrationBean<RequestResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        bean.addUrlPatterns("/*");
        return bean;
    }
}
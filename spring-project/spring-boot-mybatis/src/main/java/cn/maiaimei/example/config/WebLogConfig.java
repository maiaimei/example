package cn.maiaimei.example.config;

import cn.maiaimei.framework.spring.boot.web.filter.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;

@Configuration
public class WebLogConfig {
    @Bean
    public FilterRegistrationBean requestResponseLoggingFilter() {
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setExcludeUris(Arrays.asList("/swagger", "/h2-console"));
        requestResponseLoggingFilter.setIncludeRemoteHost(true);
        requestResponseLoggingFilter.setIncludeDevice(true);
        requestResponseLoggingFilter.setIncludeOs(true);
        //requestResponseLoggingFilter.setIncludeResponseBody(true);

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }
}

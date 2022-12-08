package cn.maiaimei.example.config;

import cn.maiaimei.example.filter.MyFilter;
import cn.maiaimei.example.filter.MyRequestLoggingFilter;
import cn.maiaimei.framework.web.http.RequestResponseLoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistrationBean() {
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyFilter());
        bean.setName("myFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseLoggingFilterRegistrationBean() {
        FilterRegistrationBean<RequestResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setExcludeUris(Arrays.asList("/swagger", "/v2/api-docs"));
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }

    /**
     * 需要加配置才能生效，配置如下：
     * logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter: debug
     */
    @ConditionalOnExpression("false")
    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(99999);
        return loggingFilter;
    }

    @ConditionalOnExpression("false")
    @Bean
    public MyRequestLoggingFilter myRequestLoggingFilter() {
        MyRequestLoggingFilter loggingFilter = new MyRequestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(99999);
        return loggingFilter;
    }
}

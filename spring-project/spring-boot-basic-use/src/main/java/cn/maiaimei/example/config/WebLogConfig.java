package cn.maiaimei.example.config;

import cn.maiaimei.framework.spring.boot.web.filter.RequestResponseLoggingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Configuration
public class WebLogConfig {
    @Bean
    public FilterRegistrationBean requestResponseLoggingFilter() {
        RequestResponseLoggingFilter requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setExcludeUris(Arrays.asList("/swagger"));
        requestResponseLoggingFilter.setIncludeRemoteHost(true);
        requestResponseLoggingFilter.setIncludeDevice(true);
        requestResponseLoggingFilter.setIncludeOs(true);
        requestResponseLoggingFilter.setIncludeResponseBody(true);

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(requestResponseLoggingFilter);
        bean.setName("requestResponseLoggingFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }

    /**
     * 需要加配置才能生效，配置如下：
     * logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter: debug
     *
     * @return
     */
    //@Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(99999);
        return loggingFilter;
    }

    //@Bean
    @ConditionalOnMissingBean(CommonsRequestLoggingFilter.class)
    public MyRequestLoggingFilter myRequestLoggingFilter() {
        MyRequestLoggingFilter loggingFilter = new MyRequestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(99999);
        return loggingFilter;
    }

    public static class MyRequestLoggingFilter extends AbstractRequestLoggingFilter {
        @Override
        protected void beforeRequest(HttpServletRequest request, String message) {
            log.info(message);
        }

        @Override
        protected void afterRequest(HttpServletRequest request, String message) {
            log.info(message);
        }
    }
}

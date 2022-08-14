package cn.maiaimei.example.config;

import cn.maiaimei.example.component.RequestHandlerInterceptor;
import cn.maiaimei.example.component.RequestWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHandlerInterceptor());
    }

    /**
     * 注册过滤器
     * 1、@ServletComponentScan + @WebFilter，这种方式注册的过滤器无法保证过滤器之间的执行顺序
     * 2、FilterRegistrationBean，通过setOrder保证过滤器之间的执行顺序
     */
    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> requestWrapperFilter() {
        FilterRegistrationBean<RequestWrapperFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestWrapperFilter());
        bean.setName("requestWrapperFilter");
        bean.setOrder(1);
        bean.addUrlPatterns("/*");
        return bean;
    }

}

package cn.maiaimei.example.servlet.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器注册方式：
 * 1、@ServletComponentScan + @WebFilter，这种方式注册的过滤器无法保证过滤器之间的执行顺序
 * 2、FilterRegistrationBean，通过setOrder保证过滤器之间的执行顺序
 */
@Configuration
public class FilterConfig {
    //@Bean
    public FilterRegistrationBean xxxFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new XxxFilter());
        bean.setName("xxxFilter");
        bean.setOrder(3);
        bean.addUrlPatterns("/*");
        return bean;
    }

    //@Bean
    public FilterRegistrationBean yyyFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new YyyFilter());
        bean.setName("yyyFilter");
        bean.setOrder(2);
        bean.addUrlPatterns("/*");
        return bean;
    }

    //@Bean
    public FilterRegistrationBean zzzFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new ZzzFilter());
        bean.setName("zzzFilter");
        bean.setOrder(1);
        bean.addUrlPatterns("/*");
        return bean;
    }
}

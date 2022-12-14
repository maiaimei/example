package cn.maiaimei.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 定制与接管SpringMVC
 * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-config
 */
@ComponentScan(
        basePackages = "cn.maiaimei.example",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)
        },
        useDefaultFilters = false
)
@EnableWebMvc
public class MyServletConfig implements WebMvcConfigurer {
    /**
     * 配置视图解析器
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // Register JSP view resolver using a default view name prefix of "/WEB-INF/" and a default suffix of ".jsp".
        // registry.jsp();
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    /**
     * 静态资源访问，如图片等
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}

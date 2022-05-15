package cn.maiaimei.example.config;

import cn.maiaimei.example.interceptor.AaaHandlerInterceptor;
import cn.maiaimei.example.interceptor.BbbHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

//@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Resource
    private AaaHandlerInterceptor aaaHandlerInterceptor;

    @Resource
    private BbbHandlerInterceptor bbbHandlerInterceptor;

    /**
     * 注册拦截器
     * 拦截器的注册顺序就是拦截器的执行顺序
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bbbHandlerInterceptor).addPathPatterns("/*");
        registry.addInterceptor(aaaHandlerInterceptor).addPathPatterns("/*");
    }
}

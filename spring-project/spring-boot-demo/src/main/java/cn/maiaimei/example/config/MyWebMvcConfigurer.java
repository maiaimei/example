package cn.maiaimei.example.config;

import cn.maiaimei.example.interceptor.MyHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    private final MyHandlerInterceptor myHandlerInterceptor;

    @Autowired
    public MyWebMvcConfigurer(MyHandlerInterceptor myHandlerInterceptor) {
        this.myHandlerInterceptor = myHandlerInterceptor;
    }

    /**
     * 注册拦截器
     * 拦截器的注册顺序就是拦截器的执行顺序
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myHandlerInterceptor).addPathPatterns("/*");
    }
}

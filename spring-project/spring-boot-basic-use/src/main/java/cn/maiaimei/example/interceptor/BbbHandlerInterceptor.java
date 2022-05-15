package cn.maiaimei.example.interceptor;

import cn.maiaimei.example.config.MyWebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器定义后，需要注册才能使用，见 {@link MyWebMvcConfigurer}
 */
@Slf4j
@Component
public class BbbHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("===== Spring拦截器 ===== HandlerInterceptor.preHandle, before the execution of a handler");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("===== Spring拦截器 ===== HandlerInterceptor.postHandle, after successful execution of a handler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("===== Spring拦截器 ===== HandlerInterceptor.afterCompletion, after completion of request processing, that is, after rendering the view");
    }
}

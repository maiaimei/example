package cn.maiaimei.example.component;

import cn.maiaimei.example.constant.GlobalConstant;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * 生成全局 traceId：
 * 方法一：在 {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)} 中加
 * 方法二：用 aop 拦截所有 controller 请求，生成全局traceId，@Pointcut("execution(* *.*.controller..*.*(..))")
 */
public class CustomFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put(GlobalConstant.TRACE_ID, UUID.randomUUID().toString().replaceAll("-", ""));
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(GlobalConstant.TRACE_ID);
        }
    }
}

package cn.maiaimei.example.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request instanceof RequestWrapper) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            String method = requestWrapper.getMethod();
            String uri = requestWrapper.getRequestURI();
            String queryString = requestWrapper.getQueryString();
            String payload = requestWrapper.getPayload();
            log.info("uri: {}, method: {}, queryString: {}, payload: {}", uri, method, queryString, payload);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

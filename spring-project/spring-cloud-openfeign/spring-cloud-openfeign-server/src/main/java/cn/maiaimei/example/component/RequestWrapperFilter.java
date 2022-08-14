package cn.maiaimei.example.component;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class RequestWrapperFilter implements Filter {
    private static final String TRACE_ID = "traceId";
    private static final String HYPHEN = "-";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = getTraceId(request);
        MDC.put(TRACE_ID, traceId);

        RequestWrapper requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
            requestWrapper.addHeader(TRACE_ID, traceId);
        }
        
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }

        MDC.remove(TRACE_ID);
    }

    @Override
    public void destroy() {

    }

    private String getTraceId(ServletRequest request) {
        String traceId = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            traceId = httpServletRequest.getHeader(TRACE_ID);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replaceAll(HYPHEN, StringUtils.EMPTY);
        }
        return traceId;
    }
}
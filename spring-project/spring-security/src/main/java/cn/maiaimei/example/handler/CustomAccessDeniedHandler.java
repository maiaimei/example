package cn.maiaimei.example.handler;

import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.beans.ResultUtils;
import cn.maiaimei.framework.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Result result = ResultUtils.error(HttpStatus.FORBIDDEN.value(), "无权限访问");
        response.getWriter().write(JsonUtils.stringify(result));
    }
}

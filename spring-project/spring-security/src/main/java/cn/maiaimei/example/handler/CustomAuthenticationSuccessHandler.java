package cn.maiaimei.example.handler;

import cn.maiaimei.example.constant.BaseConstant;
import cn.maiaimei.example.model.UserDetail;
import cn.maiaimei.example.service.RedisService;
import cn.maiaimei.example.util.AuthenticationUtils;
import cn.maiaimei.example.util.JwtUtils;
import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.beans.ResultUtils;
import cn.maiaimei.framework.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${app.sso.enabled:false}")
    private boolean ssoEnabled;

    @Autowired
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetail user = (UserDetail) authentication.getPrincipal();

        String key = AuthenticationUtils.getRedisKey4User(user.getUsername(), user.getUid());
        redisService.expire(key, BaseConstant.EXPIRATION, TimeUnit.SECONDS);

        if (ssoEnabled || user.isSsoEnabled()) {
            Set<String> keys = redisService.keys(String.format("user:%s:*", user.getUsername()));
            keys = keys.stream().filter(k -> !key.equals(k)).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(keys)) {
                redisService.delete(keys);
            }
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(BaseConstant.JWT_CLAIMS_UID, user.getUid());
        claims.put(BaseConstant.JWT_CLAIMS_USN, user.getUsername());
        String token = JwtUtils.createToken(claims);

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Result result = ResultUtils.success("登录成功", token);
        response.getWriter().write(JsonUtils.stringify(result));
    }
}

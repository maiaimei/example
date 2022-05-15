package cn.maiaimei.example.handler;

import cn.maiaimei.example.service.RedisService;
import cn.maiaimei.example.util.AuthenticationUtils;
import cn.maiaimei.example.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    private RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = AuthenticationUtils.getToken(request);
        if (!StringUtils.isBlank(token) && JwtUtils.verifyToken(token)) {
            String key = AuthenticationUtils.getRedisKey4User(token);
            redisService.delete(key);
        }
    }
}

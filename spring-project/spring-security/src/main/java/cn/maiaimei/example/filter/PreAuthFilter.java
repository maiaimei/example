package cn.maiaimei.example.filter;

import cn.maiaimei.example.constant.BaseConstant;
import cn.maiaimei.example.model.UserInfo;
import cn.maiaimei.example.service.RedisService;
import cn.maiaimei.example.util.AuthenticationUtils;
import cn.maiaimei.example.util.JwtUtils;
import cn.maiaimei.framework.util.JsonUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class PreAuthFilter extends OncePerRequestFilter {
    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = AuthenticationUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }
        if (!JwtUtils.verifyToken(token)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        if (authentication == null) {
            chain.doFilter(request, response);
            return;
        }
        // 保存认证对象 (一般用于自定义认证成功保存认证对象)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    @SneakyThrows
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String key = AuthenticationUtils.getRedisKey4User(token);
        String val = redisService.get(key);
        if (StringUtils.isBlank(val)) {
            return null;
        }
        UserInfo userInfo = JsonUtils.parse(val, UserInfo.class);
        redisService.expire(key, BaseConstant.EXPIRATION, TimeUnit.SECONDS);
        User principal = new User(userInfo.getUsername(), userInfo.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.getAuthorities()));
        return new UsernamePasswordAuthenticationToken(principal, null, AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.getAuthorities()));
    }
}
package cn.maiaimei.example.util;

import cn.maiaimei.example.constant.BaseConstant;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtils {
    public static String getToken(HttpServletRequest request) {
        String token = StringUtils.defaultIfBlank(request.getHeader(BaseConstant.X_AUTH_TOKEN_HEADER), request.getParameter(BaseConstant.TOKEN_PARAMETER));
        return token;
    }

    public static String getRedisKey4User(String token) {
        Claims claims = JwtUtils.parseToken(token);
        String uid = claims.get(BaseConstant.JWT_CLAIMS_UID).toString();
        String usn = claims.get(BaseConstant.JWT_CLAIMS_USN).toString();
        return AuthenticationUtils.getRedisKey4User(usn, uid);
    }

    public static String getRedisKey4User(String username, String uuid) {
        return String.format(BaseConstant.USER_REDIS_KEY, username, uuid);
    }
}

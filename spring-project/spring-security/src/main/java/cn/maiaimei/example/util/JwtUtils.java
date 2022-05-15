package cn.maiaimei.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtils {
    private static final String secretKey = "09f8%@ed6(2";

    public static String createToken(Map<String, Object> claims) {
        String compact = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return compact;
    }

    public static String createToken(Map<String, Object> claims, Date issuedAt, Date expiration) {
        String compact = Jwts.builder()
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return compact;
    }

    public static boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
            return false;
        }
    }

    public static Claims parseToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return claims;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
            return null;
        }
    }
}

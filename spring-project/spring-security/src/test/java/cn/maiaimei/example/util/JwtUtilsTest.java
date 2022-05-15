package cn.maiaimei.example.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtUtilsTest {
    @Test
    public void testCreateToken1() {
        Map<String, Object> claims = getClaims();

        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1biI6ImFkbWluIiwic2lkIjoiNjY2In0.gLRsGvWfwtxmmfDYClyD74wawoqzWxTioZWy6c9BgBk";
        String actualToken = JwtUtils.createToken(claims);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testCreateToken2() {
        Map<String, Object> claims = getClaims();

        LocalDateTime now = LocalDateTime.of(2022, 5, 2, 11, 1, 1);
        ZoneId zoneId = ZoneId.systemDefault();
        Date issuedAt = Date.from(now.atZone(zoneId).toInstant());
        Date expiration = Date.from(now.plusMinutes(1).atZone(zoneId).toInstant());

        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NTE0NjA0NjEsImV4cCI6MTY1MTQ2MDUyMSwidW4iOiJhZG1pbiIsInNpZCI6IjY2NiJ9.5Jq6gwZB0jXSRyNC_583Wyo4GI1neTBgt-fT7dkKSUI";
        String actualToken = JwtUtils.createToken(claims, issuedAt, expiration);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testVerifyToken1() {
        Map<String, Object> claims = getClaims();

        String token = JwtUtils.createToken(claims);
        assertEquals(Boolean.TRUE, JwtUtils.verifyToken(token));
    }

    @Test
    public void testVerifyToken2() {
        Map<String, Object> claims = getClaims();

        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Date issuedAt = Date.from(now.atZone(zoneId).toInstant());
        Date expiration = Date.from(now.plusMinutes(1).atZone(zoneId).toInstant());

        String token = JwtUtils.createToken(claims, issuedAt, expiration);
        assertEquals(Boolean.TRUE, JwtUtils.verifyToken(token));
    }

    private Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("un", "admin");
        claims.put("sid", "666");
        return claims;
    }
}

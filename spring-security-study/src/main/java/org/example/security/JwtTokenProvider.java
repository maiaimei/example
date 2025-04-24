package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret-key}")
  private String jwtSecretKey;

  @Value("${jwt.expirate-in-ms}")
  private String jwtExpirateInMs;

  public String generateToken(Date issuedAt, Date expirationTime, Map<String, String> claims) {
    return Jwts.builder()
        .issuedAt(issuedAt)
        .expiration(expirationTime)
        .claims(claims)
        .signWith(getSigningKey())
        .compact();
  }

  public String generateToken(String subject, Date issuedAt, Date expirationTime, Map<String, String> claims) {
    return Jwts.builder()
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expirationTime)
        .claims(claims)
        .signWith(getSigningKey())
        .compact();
  }

  public String generateToken(String id, String subject, Date issuedAt, Date expirationTime,
      Map<String, String> claims) {
    return Jwts.builder()
        .id(id)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expirationTime)
        .claims(claims)
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateToken(String token) {
    boolean result = false;
    try {
      resolveToken(token);
      result = true;
    } catch (Exception e) {
      log.error("Error occurs in validateToken: {}", e.getMessage(), e);
    }
    return result;
  }

  public Claims resolveToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getClaimValue(String token, String claimName) {
    final Claims claims = resolveToken(token);
    return claims.get(claimName, String.class);
  }

  public String getClaimValue(Claims claims, String claimName) {
    return claims.get(claimName, String.class);
  }

  public String refreshToken(String token, Date issuedAt, Date expirationTime) {
    try {
      // Validate the refresh token first
      if (!validateToken(token)) {
        throw new JwtException("Invalid refresh token");
      }
      // Parse the refresh token to get claims
      Claims claims = resolveToken(token);
      // Copy relevant claims from refresh token to new token
      Map<String, Object> additionalClaims = new HashMap<>();
      List<String> registeredClaimKeys = List.of(
          Claims.ID,
          Claims.SUBJECT,
          Claims.AUDIENCE,
          Claims.ISSUER,
          Claims.ISSUED_AT,
          Claims.NOT_BEFORE,
          Claims.EXPIRATION
      );
      claims.forEach((key, value) -> {
        if (!registeredClaimKeys.contains(key)) {
          additionalClaims.put(key, value);
        }
      });
      // Generate new access token
      return Jwts.builder()
          .id(Optional.ofNullable(claims.get(Claims.ID)).orElse("").toString())
          .subject(Optional.ofNullable(claims.get(Claims.SUBJECT)).orElse("").toString())
          .issuedAt(issuedAt)
          .expiration(expirationTime)
          .claims(additionalClaims)
          .signWith(getSigningKey())
          .compact();
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtException("Error refreshing token", e);
    }
  }

  public Date getExpirationTime(Date date) {
    final Instant instant = date.toInstant().plusMillis(Long.parseLong(jwtExpirateInMs));
    return Date.from(instant);
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = this.jwtSecretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
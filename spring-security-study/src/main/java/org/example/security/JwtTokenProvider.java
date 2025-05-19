package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

  private static final String EXPECTED_ISSUER = "your-issuer";
  private static final String EXPECTED_AUDIENCE = "your-audience";
  private static final List<String> REQUIRED_CLAIMS = Arrays.asList(Claims.ID, Claims.SUBJECT, "your-custom-claim");
  private static final long LEEWAY_MS = 60 * 1000; // 1-minute leeway for time validation
  private static final long MAX_REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds
  private static final long MIN_REFRESH_TOKEN_DURATION = 60 * 1000; // 1 minute in milliseconds
  private static final int MAX_REFRESH_COUNT = 5; // Maximum allowed refreshes

  //private TokenBlacklistService tokenBlacklistService; // Dependency for blacklist checks

  private static final List<String> REGISTERED_CLAIM_KEYS = List.of(
      Claims.ID,
      Claims.SUBJECT,
      Claims.AUDIENCE,
      Claims.ISSUER,
      Claims.ISSUED_AT,
      Claims.NOT_BEFORE,
      Claims.EXPIRATION);

  /**
   * 使用安全的密钥管理服务（如 AWS KMS、Azure Key Vault）来存储和管理密钥。
   * <p>
   * 避免将密钥直接存储在配置文件中，改为通过环境变量或密钥管理服务动态加载。
   */
  @Value("${jwt.secret-key}")
  private String jwtSecretKey;

  @Value("${jwt.expirate-in-ms}")
  private long jwtExpirateInMs;

  /**
   * Generate a JWT token with the provided claims.
   */
  public String generateToken(Date issuedAt, Date expirationTime, Map<String, String> claims) {
    return buildToken(null, null, issuedAt, expirationTime, claims);
  }

  /**
   * Generate a JWT token with subject and claims.
   */
  public String generateToken(String subject, Date issuedAt, Date expirationTime, Map<String, String> claims) {
    return buildToken(null, subject, issuedAt, expirationTime, claims);
  }

  /**
   * Generate a JWT token with ID, subject, and claims.
   */
  public String generateToken(String id, String subject, Date issuedAt, Date expirationTime,
      Map<String, String> claims) {
    return buildToken(id, subject, issuedAt, expirationTime, claims);
  }

  /**
   * Validates the given JWT token.
   *
   * @param token The JWT token to validate.
   * @return true if the token is valid, false otherwise.
   */
  public boolean validateToken(String token) {
    try {
      // 1. Parse and verify the token signature
      Claims claims = resolveToken(token);

      // 2. Validate expiration time with leeway
      if (claims.getExpiration() != null && claims.getExpiration().getTime() < (new Date().getTime() - LEEWAY_MS)) {
        log.error("JWT token is expired");
        return false;
      }

      // 3. Validate not-before time with leeway
      if (claims.getNotBefore() != null && claims.getNotBefore().getTime() > (new Date().getTime() + LEEWAY_MS)) {
        log.error("JWT token not yet valid");
        return false;
      }

      // 4. Validate required claims
      validateRequiredClaims(claims);

      // 5. Validate issuer (if required)
      String issuer = claims.getIssuer();
      if (issuer != null && !EXPECTED_ISSUER.equals(issuer)) {
        log.error("Invalid token issuer");
        return false;
      }

      // 6. Validate audience (if required)
      Set<String> audiences = claims.getAudience();
      if (audiences != null && !audiences.isEmpty() && !audiences.contains(EXPECTED_AUDIENCE)) {
        log.error("Invalid token audience. Expected: {}, Found: {}", EXPECTED_AUDIENCE, audiences);
        return false;
      }

      // 7. Validate JWT ID (jti) to prevent replay attacks
      String jti = claims.getId();
      if (!isJtiValid(jti)) {
        log.error("Invalid or reused JWT ID (jti)");
        return false;
      }

      return true;

    } catch (ExpiredJwtException e) {
      log.error("JWT token expired");
      return false;
    } catch (SignatureException e) {
      log.error("JWT signature validation failed");
      return false;
    } catch (MalformedJwtException e) {
      log.error("Malformed JWT token");
      return false;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("JWT token validation failed");
      return false;
    }
  }

  /**
   * Parses and resolves the JWT token to extract claims.
   * <p>
   * This method also verifies the token signature.
   *
   * @param token The JWT token to parse.
   * @return The claims extracted from the token.
   */
  public Claims resolveToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .requireIssuer(EXPECTED_ISSUER)  // Enforce issuer validation if required
        .requireAudience(EXPECTED_AUDIENCE)  // Enforce audience validation if required
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Get a specific claim value from the token.
   */
  public String getClaimValue(String token, String claimName) {
    return resolveToken(token).get(claimName, String.class);
  }

  /**
   * Get a specific claim value from the Claims object.
   */
  public String getClaimValue(Claims claims, String claimName) {
    return claims.get(claimName, String.class);
  }

  /**
   * Refreshes a JWT token by validating and generating a new token with updated claims.
   *
   * @param token          The JWT token to refresh.
   * @param issuedAt       The new issued-at timestamp.
   * @param expirationTime The new expiration timestamp.
   * @return The refreshed JWT token.
   */
  public String refreshToken(String token, Date issuedAt, Date expirationTime) {
    try {
      // 1. Validate basic parameters
      validateRefreshParameters(token, issuedAt, expirationTime);

      // 2. Parse and validate the token
      Claims claims = resolveToken(token);

      // 3. Ensure the token is a refresh token
      if (!isRefreshToken(claims)) {
        log.error("Attempt to refresh with non-refresh token");
        throw new JwtException("Not a refresh token");
      }

      // 4. Validate the token's status
      validateRefreshTokenStatus(claims);

      // 5. Build new claims for the refreshed token
      Map<String, Object> newClaims = buildRefreshedClaims(claims, issuedAt, expirationTime);

      // 6. Generate and return the new token
      return buildToken(
          claims.getId(),
          claims.getSubject(),
          issuedAt,
          expirationTime,
          newClaims
      );

    } catch (JwtException | IllegalArgumentException e) {
      log.error("Error refreshing token");
      throw new JwtException("Error refreshing token", e);
    }
  }

  /**
   * Calculate the expiration time based on the provided date.
   */
  public Date getExpirationTime(Date date) {
    Instant instant = date.toInstant().plusMillis(jwtExpirateInMs);
    return Date.from(instant);
  }

  /**
   * Builds a new JWT token with the given parameters.
   *
   * @param id             The token ID.
   * @param subject        The subject of the token.
   * @param issuedAt       The issued-at timestamp.
   * @param expirationTime The expiration timestamp.
   * @param claims         The claims to include in the token.
   * @return The generated JWT token.
   */
  private String buildToken(String id, String subject, Date issuedAt, Date expirationTime, Map<String, ?> claims) {
    return Jwts.builder()
        .id(id)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expirationTime)
        .claims(claims)
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Builds the refreshed claims for the new token.
   *
   * @param oldClaims      The claims from the old token.
   * @param issuedAt       The new issued-at timestamp.
   * @param expirationTime The new expiration timestamp.
   * @return A map of refreshed claims.
   */
  private Map<String, Object> buildRefreshedClaims(Claims oldClaims, Date issuedAt, Date expirationTime) {
    Map<String, Object> newClaims = new HashMap<>();

    // Copy required claims from the old token
    copyRequiredClaims(oldClaims, newClaims);

    // Update refresh-related claims
    newClaims.put("token_type", "refresh");
    newClaims.put("refresh_count", getRefreshCount(oldClaims) + 1);
    newClaims.put("previous_token_id", oldClaims.getId());
    newClaims.put("refresh_time", issuedAt.getTime());

    return newClaims;
  }

  /**
   * Copies required claims from the old token to the new token.
   *
   * @param source      The source claims.
   * @param destination The destination map for the new claims.
   */
  private void copyRequiredClaims(Claims source, Map<String, Object> destination) {
    Arrays.asList("sub", "roles", "permissions", "user_id")
        .forEach(claim -> {
          Object value = source.get(claim);
          if (value != null) {
            destination.put(claim, value);
          }
        });
  }

  /**
   * Retrieves the refresh count from the token claims.
   *
   * @param claims The claims extracted from the token.
   * @return The refresh count.
   */
  private int getRefreshCount(Claims claims) {
    return Optional.ofNullable(claims.get("refresh_count"))
        .map(count -> (Integer) count)
        .orElse(0);
  }

  /**
   * Validates the required claims in the JWT.
   *
   * @param claims The claims extracted from the JWT.
   */
  private void validateRequiredClaims(Claims claims) {
    // Check if all required claims are present
    for (String claim : REQUIRED_CLAIMS) {
      if (claims.get(claim) == null) {
        throw new JwtException(claim + " claim is missing");
      }
    }

    // Validate custom claims
    String userId = claims.get("userId", String.class);
    if (userId == null || userId.isEmpty()) {
      throw new JwtException("userId claim is missing or invalid");
    }
  }

  /**
   * Validates the parameters for refreshing a token.
   *
   * @param token          The JWT token.
   * @param issuedAt       The issued-at timestamp.
   * @param expirationTime The expiration timestamp.
   */
  private void validateRefreshParameters(String token, Date issuedAt, Date expirationTime) {
    if (!StringUtils.hasText(token)) {
      throw new IllegalArgumentException("Token cannot be blank");
    }

    if (issuedAt == null || expirationTime == null) {
      throw new IllegalArgumentException("Token timestamps cannot be null");
    }

    // Validate issuedAt and expirationTime with leeway
    if (issuedAt.getTime() - LEEWAY_MS > expirationTime.getTime()) {
      throw new IllegalArgumentException("Issued at time cannot be after expiration time");
    }

    // Validate duration within allowed range
    long duration = expirationTime.getTime() - issuedAt.getTime();
    if (duration > MAX_REFRESH_TOKEN_DURATION || duration < MIN_REFRESH_TOKEN_DURATION) {
      throw new IllegalArgumentException("Invalid token duration");
    }
  }

  /**
   * Validates the status of the refresh token.
   *
   * @param claims The claims extracted from the token.
   */
  private void validateRefreshTokenStatus(Claims claims) {
    // Check if the token is blacklisted
    if (isTokenBlacklisted(claims.getId())) {
      throw new JwtException("Token has been revoked");
    }

    // Validate refresh count
    Integer refreshCount = (Integer) claims.get("refresh_count");
    if (refreshCount == null || refreshCount < 0 || refreshCount >= MAX_REFRESH_COUNT) {
      throw new JwtException("Maximum refresh limit reached or invalid refresh count");
    }
  }

  /**
   * Checks if the token is a refresh token.
   *
   * @param claims The claims extracted from the token.
   * @return true if the token is a refresh token, false otherwise.
   */
  private boolean isRefreshToken(Claims claims) {
    return Optional.ofNullable(claims.get("token_type"))
        .map(type -> "refresh".equals(type.toString()))
        .orElse(false);
  }

  /**
   * Checks if the token is blacklisted.
   *
   * @param tokenId The token ID.
   * @return true if the token is blacklisted, false otherwise.
   */
  private boolean isTokenBlacklisted(String tokenId) {
    // Implement token blacklist check logic
    // return tokenBlacklistService.isBlacklisted(tokenId);
    return false;
  }

  /**
   * Validates the JWT ID (jti) to prevent replay attacks.
   *
   * @param jti The JWT ID to validate.
   * @return true if the jti is valid, false otherwise.
   */
  private boolean isJtiValid(String jti) {
    // Implement a mechanism to check if the jti has already been used
    // Example: Check the jti against a database or cache
    return true; // Default to true for now; implement actual logic as needed
  }

  /**
   * Retrieves the signing key used to verify the JWT signature.
   *
   * @return The secret key.
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
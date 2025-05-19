package org.example.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EncoderInitializationException;
import org.example.exception.InvalidPasswordFormatException;
import org.example.exception.PasswordEncoderException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Slf4j
public class SecureBCryptPasswordEncoder implements PasswordEncoder {

  private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
  private static final int BCRYPT_STRENGTH = 14;
  private static final int BYTES_LENGTH = 32;
  private static final int WARMUP_ROUNDS = 10;

  private final int strength;

  private final BCryptVersion version;

  private final SecureRandom random;

  public SecureBCryptPasswordEncoder() {
    this.strength = BCRYPT_STRENGTH;
    this.version = BCryptVersion.$2B;
    this.random = initializeSecureRandom();
  }

  @Override
  public String encode(CharSequence rawPassword) {
    validatePassword(rawPassword, "rawPassword");
    try {
      log.debug("Encoding password with BCrypt");
      // 1. 加密原始密码
      String salt = getSalt();
      final String encodedPassword = BCrypt.hashpw(rawPassword.toString(), salt);
      // 2. 使用Base64编码加密后的密码
      return Base64.getEncoder().encodeToString(
          encodedPassword.getBytes(StandardCharsets.UTF_8));
    } catch (IllegalArgumentException e) {
      String msg = "Invalid password format: password contains invalid characters";
      log.error(msg, e);
      throw new InvalidPasswordFormatException(msg, e);
    } catch (Exception e) {
      String msg = "Unexpected error during password encoding";
      log.error(msg, e);
      throw new PasswordEncoderException(msg, e);
    }
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    validatePassword(rawPassword, "rawPassword");
    validatePassword(encodedPassword, "encodedPassword");

    try {
      // 1. Base64解码存储的密码
      byte[] encodedPasswordBytes = Base64.getDecoder().decode(encodedPassword);
      String actualEncodedPassword = new String(encodedPasswordBytes, StandardCharsets.UTF_8);
      if (!this.BCRYPT_PATTERN.matcher(actualEncodedPassword).matches()) {
        log.warn("Encoded password does not look like BCrypt");
        return false;
      }
      // 2. 验证密码
      return BCrypt.checkpw(rawPassword.toString(), actualEncodedPassword);
    } catch (IllegalArgumentException e) {
      String msg = String.format("Invalid Base64 encoded password: %s", e.getMessage());
      log.error(msg, e);
      throw new InvalidPasswordFormatException(msg, e);
    } catch (Exception e) {
      String msg = "Unexpected error during password matching";
      log.error(msg, e);
      throw new PasswordEncoderException(msg, e);
    }
  }

  private String getSalt() {
    return BCrypt.gensalt(this.version.getVersion(), this.strength, this.random);
  }

  private void validatePassword(CharSequence password, String paramName) {
    if (Objects.isNull(password)) {
      String msg = String.format("%s cannot be null", paramName);
      log.warn(msg);
      throw new InvalidPasswordFormatException(msg);
    }
    if (password.isEmpty()) {
      String msg = String.format("%s cannot be empty", paramName);
      log.warn(msg);
      throw new InvalidPasswordFormatException(msg);
    }
    if (StringUtils.containsWhitespace(password)) {
      String msg = String.format("%s contains whitespace", paramName);
      log.warn(msg);
      throw new InvalidPasswordFormatException(msg);
    }
  }

  private SecureRandom initializeSecureRandom() {
    // Create DRBG using BouncyCastle provider
    SecureRandom secureRandom = new CTRDRBGSecureRandomV3();
    // Perform warmup
    warmupSecureRandom(secureRandom);
    return secureRandom;
  }

  private void warmupSecureRandom(SecureRandom secureRandom) {
    byte[] buffer = new byte[BYTES_LENGTH];
    try {
      for (int i = 0; i < WARMUP_ROUNDS; i++) {
        secureRandom.nextBytes(buffer);
      }
      log.debug("Completed {} warmup rounds", WARMUP_ROUNDS);
    } catch (Exception e) {
      String msg = "Error during SecureRandom warmup";
      log.error(msg, e);
      throw new EncoderInitializationException(msg, e);
    } finally {
      Arrays.fill(buffer, (byte) 0);
    }
  }

}

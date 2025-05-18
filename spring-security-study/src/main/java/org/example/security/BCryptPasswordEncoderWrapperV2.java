package org.example.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EncoderInitializationException;
import org.example.exception.InvalidPasswordFormatException;
import org.example.exception.PasswordEncoderException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Slf4j
public class BCryptPasswordEncoderWrapperV2 implements PasswordEncoder {

  // 因为简单的"DRBG"可能在某些JVM中不被支持，建议使用更具体的算法名称。例如：DRBG-SHA-512
  private static final String DRBG_ALGORITHM = "DRBG";
  private static final int BCRYPT_STRENGTH = 14;
  private static final int BYTES_LENGTH = 16;
  private static final int WARMUP_ROUNDS = 10;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public BCryptPasswordEncoderWrapperV2() {
    log.debug("Initializing BCryptPasswordEncoderWrapper with strength: {}", BCRYPT_STRENGTH);
    this.bCryptPasswordEncoder = createBCryptPasswordEncoder();
  }

  @Override
  public String encode(CharSequence rawPassword) {
    validatePassword(rawPassword, "rawPassword");
    try {
      log.debug("Encoding password with BCrypt");
      // 1. 使用BCrypt加密原始密码
      final String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
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
      byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
      String bcryptHash = new String(decodedBytes, StandardCharsets.UTF_8);
      // 2. 使用BCrypt验证密码
      return bCryptPasswordEncoder.matches(rawPassword, bcryptHash);
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

  private BCryptPasswordEncoder createBCryptPasswordEncoder() {
    final SecureRandom secureRandom = initializeSecureRandom();
    try {
      final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(
          BCryptVersion.$2B,
          BCRYPT_STRENGTH,
          secureRandom);
      log.info("BCryptPasswordEncoder initialized successfully");
      return encoder;
    } catch (Exception e) {
      String msg = "Failed to initialize BCryptPasswordEncoder";
      log.error(msg, e);
      throw new EncoderInitializationException(msg, e);
    }
  }

  private SecureRandom initializeSecureRandom() {
    try {
      SecureRandom random = SecureRandom.getInstance(DRBG_ALGORITHM);

      // Perform warmup
      warmupSecureRandom(random);

      return random;
    } catch (NoSuchAlgorithmException e) {
      log.error("Failed to initialize SecureRandom", e);
      throw new PasswordEncoderException("Failed to initialize SecureRandom", e);
    }
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

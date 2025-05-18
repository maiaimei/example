package org.example.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.PasswordEncoderException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class SecureBCryptPasswordEncoderV1 implements PasswordEncoder {

  // 因为简单的"DRBG"可能在某些JVM中不被支持，建议使用更具体的算法名称。例如：DRBG-SHA-512
  private static final String DRBG_ALGORITHM = "DRBG";
  private static final int BCRYPT_STRENGTH = 14;
  private static final int BYTES_LENGTH = 16;
  private static final int SEED_LENGTH = 32;
  private static final int WARMUP_ROUNDS = 10;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public SecureBCryptPasswordEncoderV1() {
    log.debug("Initializing BCryptPasswordEncoderWrapper with strength: {}", BCRYPT_STRENGTH);

    final SecureRandom secureRandom = initializeSecureRandom();
    try {
      warmup(secureRandom);
      bCryptPasswordEncoder = new BCryptPasswordEncoder(
          BCryptVersion.$2B,
          BCRYPT_STRENGTH,
          secureRandom);
      log.info("BCryptPasswordEncoderWrapper initialized successfully");
    } catch (Exception e) {
      log.error("Failed to initialize BCryptPasswordEncoder", e);
      throw new PasswordEncoderException("Failed to initialize password encoder", e);
    }
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
    } catch (Exception e) {
      log.error("Failed to encode password", e);
      throw new PasswordEncoderException("Password encoding failed", e);
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
      log.error("Invalid Base64 encoded password", e);
      throw new PasswordEncoderException("Invalid Base64 encoded password", e);
    } catch (Exception e) {
      log.error("Password matching failed", e);
      throw new PasswordEncoderException("Password matching failed", e);
    }
  }

  private void validatePassword(CharSequence password, String paramName) {
    if (Objects.isNull(password)) {
      log.warn("Attempt to use null {}", paramName);
      throw new IllegalArgumentException(paramName + " cannot be null");
    }
    if (password.isEmpty()) {
      log.warn("Attempt to use empty {}", paramName);
      throw new IllegalArgumentException(paramName + " cannot be empty");
    }
  }

  private SecureRandom initializeSecureRandom() {
    try {
      // 1. 使用DRBG算法创建SecureRandom实例
      SecureRandom random = SecureRandom.getInstance(DRBG_ALGORITHM);

      byte[] primarySeed = null;
      byte[] additionalEntropy = null;
      byte[] combinedSeed = null;

      try {
        // 2. 从系统默认熵源获取种子
        primarySeed = getSystemEntropy();

        // 3. 获取额外的熵源
        additionalEntropy = getAdditionalEntropy();

        // 4. 合并熵源
        combinedSeed = combineSeedSources(primarySeed, additionalEntropy);

        // 5. 提供初始熵
        random.setSeed(combinedSeed);
        return random;
      } finally {
        // 安全清除所有敏感数据
        secureWipe(primarySeed);
        secureWipe(additionalEntropy);
        secureWipe(combinedSeed);
      }

    } catch (NoSuchAlgorithmException e) {
      log.error("Failed to initialize SecureRandom", e);
      throw new PasswordEncoderException("Failed to initialize SecureRandom", e);
    }
  }

  private byte[] getSystemEntropy() {
    byte[] buffer = new byte[SEED_LENGTH];
    try {
      SecureRandom systemRandom = SecureRandom.getInstanceStrong();
      systemRandom.nextBytes(buffer);
      return buffer;
    } catch (NoSuchAlgorithmException e) {
      log.warn("Failed to get strong SecureRandom, falling back to default", e);
      new SecureRandom().nextBytes(buffer);
      return buffer;
    }
  }

  private byte[] getAdditionalEntropy() {
    String entropyBuilder = String.valueOf(System.nanoTime())
        + '-'
        + Runtime.getRuntime().totalMemory()
        + '-'
        + Runtime.getRuntime().freeMemory()
        + '-'
        + Thread.currentThread().threadId()
        + '-'
        + System.currentTimeMillis();

    return entropyBuilder.getBytes(StandardCharsets.UTF_8);
  }

  private byte[] combineSeedSources(byte[] primary, byte[] additional) {
    byte[] combined = new byte[primary.length + additional.length];
    System.arraycopy(primary, 0, combined, 0, primary.length);
    System.arraycopy(additional, 0, combined, primary.length, additional.length);
    return combined;
  }

  private void secureWipe(byte[] data) {
    if (data != null) {
      Arrays.fill(data, (byte) 0);
    }
  }

  private void warmup(SecureRandom secureRandom) {
    byte[] buffer = new byte[BYTES_LENGTH];
    try {
      for (int i = 0; i < WARMUP_ROUNDS; i++) {
        secureRandom.nextBytes(buffer);
      }
      log.debug("Completed {} warmup rounds", WARMUP_ROUNDS);
    } finally {
      secureWipe(buffer);
    }
  }

}

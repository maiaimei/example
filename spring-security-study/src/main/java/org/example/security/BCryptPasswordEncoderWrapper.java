package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class BCryptPasswordEncoderWrapper implements PasswordEncoder {

  private static final int BYTES_LENGTH = 16;
  private static final int WARMUP_ROUNDS = 10;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public BCryptPasswordEncoderWrapper() {
    final SecureRandom secureRandom = initializeSecureRandom();
    warmup(secureRandom);
    bCryptPasswordEncoder = new BCryptPasswordEncoder(BCryptVersion.$2B, 14, secureRandom);
  }

  @Override
  public String encode(CharSequence rawPassword) {
    if (Objects.isNull(rawPassword) || rawPassword.isEmpty()) {
      throw new IllegalArgumentException("rawPassword cannot be null or empty");
    }
    // 1. 使用BCrypt加密原始密码
    final String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
    // 2. 使用Base64编码加密后的密码
    return Base64.getEncoder().encodeToString(encodedPassword.getBytes());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (Objects.isNull(rawPassword) || rawPassword.isEmpty()) {
      throw new IllegalArgumentException("rawPassword cannot be null or empty");
    }
    if (Objects.isNull(encodedPassword) || encodedPassword.isEmpty()) {
      throw new IllegalArgumentException("encodedPassword cannot be null or empty");
    }
    // 1. Base64解码存储的密码
    byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
    String bcryptHash = new String(decodedBytes);
    // 2. 使用BCrypt验证密码
    return bCryptPasswordEncoder.matches(rawPassword, bcryptHash);
  }

  private SecureRandom initializeSecureRandom() {
    try {
      // 1. 使用DRBG算法创建SecureRandom实例
      SecureRandom random = SecureRandom.getInstance("DRBG");

      // 2. 从系统默认熵源获取种子
      byte[] primarySeed = getSystemEntropy();

      // 3. 获取额外的熵源
      byte[] additionalEntropy = getAdditionalEntropy();

      // 4. 合并熵源
      byte[] combinedSeed = combineSeedSources(primarySeed, additionalEntropy);

      // 5. 初始化DRBG。 setSeed 是用来添加额外的熵到随机数生成器的内部状态
      random.setSeed(combinedSeed);
      return random;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] getSystemEntropy() {
    // 使用默认SecureRandom获取系统熵
    byte[] buffer = new byte[BYTES_LENGTH];
    new SecureRandom().nextBytes(buffer);
    return buffer;
  }

  private byte[] getAdditionalEntropy() {
    // 组合多个熵源
    // System.getProperty("user.name") 是用来获取当前操作系统用户名的系统属性。
    // Windows: 返回登录用户名
    // Linux: 返回运行进程的用户名
    // Server: 可能返回服务进程用户(如 'tomcat' 或 'root')
    String entropyData = String.format("%d-%s-%s-%s",
        System.nanoTime(),
        System.getProperty("user.name"),
        Runtime.getRuntime().toString(),
        Thread.currentThread().getName());
    return entropyData.getBytes();
  }

  private byte[] combineSeedSources(byte[] primary, byte[] additional) {
    byte[] combined = new byte[primary.length + additional.length];
    System.arraycopy(primary, 0, combined, 0, primary.length);
    System.arraycopy(additional, 0, combined, primary.length, additional.length);
    return combined;
  }

  private void warmup(SecureRandom secureRandom) {
    byte[] buffer = new byte[BYTES_LENGTH];
    for (int i = 0; i < WARMUP_ROUNDS; i++) {
      secureRandom.nextBytes(buffer);
    }
    log.debug("Completed {} warmup rounds", WARMUP_ROUNDS);
  }

}

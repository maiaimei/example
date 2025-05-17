package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Java内置的DRBG实现CTR_DRBG with AES-256需要考虑以下几个关键因素:
 * <p>
 * 安全强度（Security Strength）
 * <p>
 * 熵源（Entropy Source）
 * <p>
 * 个性化字符串（Personalization String）
 * <p>
 * 重新播种（Reseeding）机制
 */
public class CTRDRBG_AES256_Test {

  public static void main(String[] args) {
    testCTR_DRBG_AES256();
    testCTR_DRBG_AES256_CompleteExample();
  }

  public static void testCTR_DRBG_AES256() {
    try {
      // 1. 获取DRBG实例
      SecureRandom secureRandom = SecureRandom.getInstance("DRBG");

      // 2. 添加额外的熵
      byte[] additionalEntropy = new byte[32];
      new SecureRandom().nextBytes(additionalEntropy);
      secureRandom.setSeed(additionalEntropy);

      // 3. 打印DRBG详细信息
      printDRBGDetails(secureRandom);

      // 4. 生成随机数测试（AES-256需要32字节）
      byte[] randomBytes = new byte[32];
      secureRandom.nextBytes(randomBytes);
      System.out.println("\nRandom sample: " + bytesToHex(randomBytes));

      // 5. 定期或在生成特定数量的随机数据后重新播种
      reseedDRBG(secureRandom);

    } catch (NoSuchAlgorithmException e) {
      System.err.println("DRBG not available: " + e.getMessage());
    }
  }

  public static void testCTR_DRBG_AES256_CompleteExample() {
    try {
      // 1. 创建个性化字符串
      byte[] personalization = "ApplicationSpecificString".getBytes();

      // 2. 初始化DRBG
      SecureRandom drbg = initDRBGWithPersonalization(personalization);

      // 3. 生成密钥材料
      byte[] key = new byte[32];  // AES-256密钥
      byte[] iv = new byte[16];   // 初始化向量

      drbg.nextBytes(key);
      drbg.nextBytes(iv);

      // 4. 定期重新播种
      reseedDRBG(drbg);

      // 5. 验证输出
      validateDRBGOutput(drbg);

      // 6. 执行连续性测试
      performContinuousTest(drbg);

    } catch (NoSuchAlgorithmException e) {
      System.err.println("DRBG initialization failed: " + e.getMessage());
    }
  }

  // 重新播种DRBG
  private static void reseedDRBG(SecureRandom secureRandom) {
    // 生成新的种子
    byte[] newSeed = new byte[32];
    // 使用不同的熵源生成种子
    SecureRandom entropySource = new SecureRandom();
    entropySource.nextBytes(newSeed);

    // 应用新种子
    secureRandom.setSeed(newSeed);
  }

  // 使用个性化字符串初始化DRBG
  private static SecureRandom initDRBGWithPersonalization(byte[] personalizationString)
      throws NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstance("DRBG");

    // 组合熵和个性化字符串
    byte[] combinedSeed = new byte[64]; // 32字节熵 + 32字节个性化字符串
    SecureRandom entropySource = new SecureRandom();
    entropySource.nextBytes(combinedSeed);

    // 合并个性化字符串
    System.arraycopy(personalizationString, 0,
        combinedSeed, 32,
        Math.min(personalizationString.length, 32));

    // 初始化DRBG
    secureRandom.setSeed(combinedSeed);

    return secureRandom;
  }

  // 验证DRBG输出
  private static void validateDRBGOutput(SecureRandom drbg) {
    byte[] sample1 = new byte[32];
    byte[] sample2 = new byte[32];

    drbg.nextBytes(sample1);
    drbg.nextBytes(sample2);

    // 验证两个样本是否不同
    boolean different = !java.util.Arrays.equals(sample1, sample2);
    System.out.println("Consecutive samples are different: " + different);
  }

  // 实现连续性测试
  private static void performContinuousTest(SecureRandom drbg) {
    byte[] previous = null;
    for (int i = 0; i < 1000; i++) {
      byte[] current = new byte[32];
      drbg.nextBytes(current);

      if (previous != null) {
        if (java.util.Arrays.equals(previous, current)) {
          System.out.println("Warning: Consecutive identical outputs detected!");
        }
      }
      previous = current.clone();
    }
  }

  private static void printDRBGDetails(SecureRandom secureRandom) {
    System.out.println("=== DRBG Implementation Details ===");
    System.out.println("Algorithm: " + secureRandom.getAlgorithm());

    Provider provider = secureRandom.getProvider();
    System.out.println("\nProvider Information:");
    System.out.println("Name: " + provider.getName());
    System.out.println("Version: " + provider.getVersion());
    System.out.println("Info: " + provider.getInfo());

    // 打印DRBG相关属性
    System.out.println("\nDRBG Properties:");
    provider.stringPropertyNames().stream()
        .filter(prop -> prop.toLowerCase().contains("drbg"))
        .forEach(prop ->
            System.out.println(prop + ": " + provider.getProperty(prop)));

    // 打印实现类
    System.out.println("\nImplementation Class: " + secureRandom.getClass().getName());
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }

  // 验证DRBG机制
  public static void verifyDRBGMechanism() {
    System.out.println("=== Available SecureRandom Services ===");
    for (Provider provider : Security.getProviders()) {
      provider.getServices().stream()
          .filter(s -> "SecureRandom".equals(s.getType()))
          .forEach(s -> {
            System.out.println("\nProvider: " + provider.getName());
            System.out.println("Algorithm: " + s.getAlgorithm());
            System.out.println("Class: " + s.getClassName());
          });
    }
  }

  // 测试DRBG的熵源
  public static void testDRBGEntropy() {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance("DRBG");

      // 生成多组随机数进行熵测试
      byte[][] samples = new byte[10][32];
      for (int i = 0; i < 10; i++) {
        secureRandom.nextBytes(samples[i]);
      }

      // 打印样本
      System.out.println("\nEntropy Test Samples:");
      for (int i = 0; i < samples.length; i++) {
        System.out.println("Sample " + (i + 1) + ": " + bytesToHex(samples[i]));
      }

    } catch (NoSuchAlgorithmException e) {
      System.err.println("Error during entropy test: " + e.getMessage());
    }
  }

  // 性能测试
  public static void performanceTest() {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance("DRBG");
      byte[] data = new byte[1024]; // 1KB

      int iterations = 1000;
      long startTime = System.nanoTime();

      for (int i = 0; i < iterations; i++) {
        secureRandom.nextBytes(data);
      }

      long endTime = System.nanoTime();
      double duration = (endTime - startTime) / 1_000_000.0; // 转换为毫秒

      System.out.printf("\nPerformance Test:\n");
      System.out.printf("Generated %d KB in %.2f ms\n", iterations, duration);
      System.out.printf("Average time per KB: %.2f ms\n", duration / iterations);

    } catch (NoSuchAlgorithmException e) {
      System.err.println("Error during performance test: " + e.getMessage());
    }
  }

  // 错误恢复测试
  public static SecureRandom getFallbackSecureRandom() {
    try {
      return SecureRandom.getInstance("DRBG");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("DRBG not available, falling back to default SecureRandom");
      return new SecureRandom();
    }
  }

  // 使用示例
  public static void usageExample() {
    SecureRandom secureRandom = getFallbackSecureRandom();

    // 生成密钥
    byte[] key = new byte[32]; // 256-bit key
    secureRandom.nextBytes(key);

    // 生成IV
    byte[] iv = new byte[16]; // 128-bit IV
    secureRandom.nextBytes(iv);

    System.out.println("Generated Key: " + bytesToHex(key));
    System.out.println("Generated IV: " + bytesToHex(iv));
  }
}

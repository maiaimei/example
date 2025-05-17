package org.example.drbg;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class DRBGUtils {

  /**
   * DRBG 类型枚举
   */
  public enum DRBGType {
    HASH_DRBG("DRBG"),    // Hash-based DRBG
    HMAC_DRBG("DRBG"),    // HMAC-based DRBG
    CTR_DRBG("DRBG");     // CTR-based DRBG

    private final String algorithmName;

    DRBGType(String algorithmName) {
      this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
      return algorithmName;
    }
  }

  /**
   * DRBG 参数配置类
   */
  public static class DRBGParameters {

    private final DRBGType type;
    private final int securityStrength;
    private final String algorithm;
    private final byte[] personalizationString;
    private final boolean predictionResistance;

    public DRBGParameters(DRBGType type, int securityStrength, String algorithm,
        byte[] personalizationString, boolean predictionResistance) {
      this.type = type;
      this.securityStrength = securityStrength;
      this.algorithm = algorithm;
      this.personalizationString = personalizationString;
      this.predictionResistance = predictionResistance;
    }
  }

  /**
   * 创建 DRBG 实例
   */
  public static SecureRandom createDRBG(DRBGParameters params) {
    try {
      // 确保 BouncyCastle 提供者已注册
      if (Security.getProvider("BC") == null) {
        Security.addProvider(new BouncyCastleProvider());
      }

      // 创建 DRBG 实例
      SecureRandom random = SecureRandom.getInstance("DRBG", "BC");

      // 如果有个性化字符串，设置种子
      if (params.personalizationString != null) {
        random.setSeed(params.personalizationString);
      }

      return random;
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new RuntimeException("Failed to create DRBG: " + e.getMessage(), e);
    }
  }

  /**
   * 创建带有默认配置的 DRBG
   */
  public static SecureRandom createDefaultDRBG() {
    return createDRBG(new DRBGParameters(
        DRBGType.HASH_DRBG,
        256,
        "SHA-512",
        null,
        true
    ));
  }

  /**
   * 性能测试
   */
  public static void performanceTest() {
    int iterations = 1000;
    byte[] bytes = new byte[1024];

    for (DRBGType type : DRBGType.values()) {
      try {
        SecureRandom random = createDRBG(new DRBGParameters(
            type,
            256,
            "SHA-512",
            ("Test-" + type.name()).getBytes(),
            true
        ));

        // 预热
        for (int i = 0; i < 100; i++) {
          random.nextBytes(new byte[1024]);
        }

        // 性能测试
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
          random.nextBytes(bytes);
        }
        long endTime = System.nanoTime();

        double milliseconds = (endTime - startTime) / 1_000_000.0;
        System.out.printf("%s:%n", type);
        System.out.printf("Total time: %.2f ms%n", milliseconds);
        System.out.printf("Average time per KB: %.3f ms%n",
            milliseconds / iterations);

      } catch (Exception e) {
        System.out.printf("%s not available: %s%n", type, e.getMessage());
      }
    }
  }

  /**
   * 生成随机字节并返回十六进制字符串
   */
  public static String generateRandomHex(int bytes) {
    SecureRandom random = createDefaultDRBG();
    byte[] randomBytes = new byte[bytes];
    random.nextBytes(randomBytes);
    return bytesToHex(randomBytes);
  }

  /**
   * 字节数组转十六进制字符串
   */
  public static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02x", b));
    }
    return result.toString();
  }

  /**
   * 线程安全的 DRBG 实例管理
   */
  private static final ThreadLocal<SecureRandom> threadLocalDRBG =
      ThreadLocal.withInitial(DRBGUtils::createDefaultDRBG);

  /**
   * 获取线程安全的 DRBG 实例
   */
  public static SecureRandom getThreadLocalDRBG() {
    return threadLocalDRBG.get();
  }

  /**
   * 使用示例
   */
  public static void main(String[] args) {
    try {
      // 1. 基本使用
      System.out.println("Basic DRBG Test:");
      String randomHex = generateRandomHex(32);
      System.out.println("Random Hex: " + randomHex);

      // 2. 线程安全使用
      System.out.println("\nThread-Safe DRBG Test:");
      Runnable task = () -> {
        SecureRandom drbg = getThreadLocalDRBG();
        byte[] bytes = new byte[16];
        drbg.nextBytes(bytes);
        System.out.println(Thread.currentThread().getName() +
            ": " + bytesToHex(bytes));
      };

      // 创建多个线程测试
      Thread[] threads = new Thread[3];
      for (int i = 0; i < threads.length; i++) {
        threads[i] = new Thread(task, "Thread-" + i);
        threads[i].start();
      }

      // 等待所有线程完成
      for (Thread thread : threads) {
        thread.join();
      }

      // 3. 性能测试
      System.out.println("\nPerformance Test:");
      performanceTest();

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}

package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;

/**
 * Test cases for DRBG (Deterministic Random Bit Generator)
 */
public class DRBGTest {

  /**
   * Test case for AES-256
   * <p>
   * 基础功能测试，没有额外的熵或个性化配置
   * </p>
   *
   * @throws NoSuchAlgorithmException if the algorithm is not available
   */
  @Test
  public void test01_CTR_DRBG_AES_256() throws NoSuchAlgorithmException {
    // 1 尝试获取DRBG实例
    // 1.1 使用 getInstance方法获取DRBG(Deterministic Random Bit Generator)的实例
    // 1.2 "DRBG"是算法名称，基于NIST SP 800-90A标准
    // 1.3 如果系统不支持DRBG，会抛出NoSuchAlgorithmException异常
    SecureRandom secureRandom = SecureRandom.getInstance("DRBG");
    // 2 通过SecurityUtils类的printSecureRandomDetails方法打印DRBG的详细信息
    SecurityUtils.printSecureRandomDetails(secureRandom);
    // 3 生成随机数
    // 3.1 创建32字节（256位）的字节数组, 用于存储生成的随机数
    // 3.2 调用SecureRandom实例的nextBytes方法生成随机数填充字节数组
    // 3.3 nextBytes方法会阻塞，直到生成足够的随机数
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);
    // 4 打印生成的随机数
    // 4.1 使用SecurityUtils类的bytesToHex方法将字节数组转换为十六进制字符串
    System.out.println("\nRandom sample: " + SecurityUtils.bytesToHex(randomBytes));
  }

  /**
   * Test case for AES-256 with additional entropy
   * <p>
   * 熵增强测试，验证DRBG的熵注入和重新播种功能
   * </p>
   */
  @Test
  public void test02_CTR_DRBG_AES_256() {
    try {
      // 1. 获取DRBG实例
      SecureRandom secureRandom = SecureRandom.getInstance("DRBG");

      // 2. 添加额外的熵
      byte[] additionalEntropy = new byte[32];
      new SecureRandom().nextBytes(additionalEntropy);
      secureRandom.setSeed(additionalEntropy);

      // 3. 打印DRBG详细信息
      SecurityUtils.printSecureRandomDetails(secureRandom);

      // 4. 生成随机数测试（AES-256需要32字节）
      byte[] randomBytes = new byte[32];
      secureRandom.nextBytes(randomBytes);
      System.out.println("\nRandom sample: " + SecurityUtils.bytesToHex(randomBytes));

      // 5. 定期或在生成特定数量的随机数据后重新播种
      reseedDRBG(secureRandom);

    } catch (NoSuchAlgorithmException e) {
      System.err.println("DRBG not available: " + e.getMessage());
    }
  }

  /**
   * Test case for AES-256 complete function
   * <p>
   * 全面测试DRBG的各项功能
   * <p>
   * 1. 使用个性化字符串初始化DRBG 2. 熵源 3. 重新播种 4. 验证输出 5. 连续性测试
   * </p>
   * </p>
   */
  @Test
  public void test03_CTR_DRBG_AES_256() {
    try {
      // 1. 创建个性化字符串
      byte[] personalization = "ApplicationSpecificString".getBytes();

      // 2. 初始化DRBG
      SecureRandom drbg = initDRBGWithPersonalization(personalization);

      // 3. 生成密钥材料
      byte[] key = new byte[32]; // AES-256密钥
      byte[] iv = new byte[16]; // 初始化向量

      drbg.nextBytes(key);
      drbg.nextBytes(iv);

      // 4. 定期重新播种
      reseedDRBG(drbg);

      // 5. 验证输出
      validateDRBGOutput(drbg);

      // 6. 执行连续性测试
      SecurityUtils.performContinuousTest(drbg);

    } catch (NoSuchAlgorithmException e) {
      System.err.println("DRBG initialization failed: " + e.getMessage());
    }
  }

  /**
   * Test case for DRBG entropy source
   * <p>
   * 评估DRBG熵源的质量，关注随机数质量和熵源特性
   * </p>
   */
  @Test
  public void testDRBGEntropySource() {
    try {
      // 试获取DRBG实例
      SecureRandom secureRandom = SecureRandom.getInstance("DRBG");

      // 生成多组随机数进行熵测试
      byte[][] samples = new byte[10][32];
      for (int i = 0; i < 10; i++) {
        secureRandom.nextBytes(samples[i]);
      }

      // 打印并分析样本
      System.out.println("\nEntropy Test Samples:");
      for (int i = 0; i < samples.length; i++) {
        System.out.println("Sample " + (i + 1) + ": " + SecurityUtils.bytesToHex(samples[i]));
      }

    } catch (NoSuchAlgorithmException e) {
      System.err.println("Error during entropy test: " + e.getMessage());
    }
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

}

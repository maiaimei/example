package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;

public class DRBGTest {

  @Test
  public void testDRBG() throws NoSuchAlgorithmException {
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
}

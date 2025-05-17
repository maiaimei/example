package org.example.drbg;

import java.security.SecureRandom;

public class DRBGExample {

  public static void main(String[] args) {
    // 1. 创建默认 DRBG
    SecureRandom drbg = DRBGUtils.createDefaultDRBG();
    byte[] randomBytes = new byte[32];
    drbg.nextBytes(randomBytes);
    System.out.println("Random bytes: " +
        DRBGUtils.bytesToHex(randomBytes));

    // 2. 使用线程安全的实例
    SecureRandom threadSafeDRBG = DRBGUtils.getThreadLocalDRBG();
    byte[] threadSafeBytes = new byte[32];
    threadSafeDRBG.nextBytes(threadSafeBytes);
    System.out.println("Thread-safe random bytes: " +
        DRBGUtils.bytesToHex(threadSafeBytes));

    // 3. 运行性能测试
    System.out.println("\nRunning performance test...");
    DRBGUtils.performanceTest();
  }
}


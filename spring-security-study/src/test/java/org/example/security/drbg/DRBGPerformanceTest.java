package org.example.security.drbg;

import java.security.SecureRandom;

public class DRBGPerformanceTest {

  public static void main(String[] args) {
    compareDRBGPerformance();
  }

  public static void compareDRBGPerformance() {
    DRBGUtils.DRBGType[] types = DRBGUtils.DRBGType.values();
    int iterations = 1000;
    byte[] bytes = new byte[1024];

    for (DRBGUtils.DRBGType type : types) {
      DRBGUtils.DRBGParameters params = new DRBGUtils.DRBGParameters(
          type,
          256,
          switch (type) {
            case HASH_DRBG -> "SHA-512";
            case HMAC_DRBG -> "HMAC-SHA512";
            case CTR_DRBG -> "AES-256";
          },
          "Test".getBytes(),
          true
      );

      try {
        SecureRandom random = DRBGUtils.createDRBG(params);

        // 预热
        for (int i = 0; i < 100; i++) {
          random.nextBytes(bytes);
        }

        // 性能测试
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
          random.nextBytes(bytes);
        }
        long endTime = System.nanoTime();

        System.out.printf("%s: %.2f ms per 1024 bytes%n",
            type, (endTime - startTime) / (iterations * 1_000_000.0));

      } catch (Exception e) {
        System.out.printf("%s not available: %s%n", type, e.getMessage());
      }
    }
  }
}

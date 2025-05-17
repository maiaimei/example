package org.example.drbg;

import java.util.HashMap;
import java.util.Map;

// Hash_DRBG 实现
public class HashDRBGExample {

  public static class HashDRBGConfig {

    private final String hashAlgorithm; // SHA-256, SHA-512 等
    private final int securityStrength; // 112, 128, 192, 256
    private final byte[] personalizationString;
    private final boolean predictionResistance;

    public HashDRBGConfig(String hashAlgorithm, int securityStrength,
        byte[] personalizationString, boolean predictionResistance) {
      this.hashAlgorithm = hashAlgorithm;
      this.securityStrength = securityStrength;
      this.personalizationString = personalizationString;
      this.predictionResistance = predictionResistance;
    }
  }

  // Hash_DRBG 支持的哈希算法
  public static final Map<String, Integer> SUPPORTED_HASH_ALGORITHMS =
      new HashMap<>() {{
        put("SHA-1", 160);    // 输出长度
        put("SHA-224", 224);
        put("SHA-256", 256);
        put("SHA-384", 384);
        put("SHA-512", 512);
      }};

  // 验证配置
  public static boolean validateConfig(HashDRBGConfig config) {
    return SUPPORTED_HASH_ALGORITHMS.containsKey(config.hashAlgorithm) &&
        config.securityStrength <= SUPPORTED_HASH_ALGORITHMS.get(config.hashAlgorithm);
  }
}

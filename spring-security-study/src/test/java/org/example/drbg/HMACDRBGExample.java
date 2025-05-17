package org.example.drbg;

import java.util.HashMap;
import java.util.Map;

// HMAC_DRBG 实现
public class HMACDRBGExample {

  public static class HMACDRBGConfig {

    private final String macAlgorithm; // HMAC-SHA256, HMAC-SHA512 等
    private final int securityStrength;
    private final byte[] personalizationString;
    private final boolean predictionResistance;

    public HMACDRBGConfig(String macAlgorithm, int securityStrength,
        byte[] personalizationString, boolean predictionResistance) {
      this.macAlgorithm = macAlgorithm;
      this.securityStrength = securityStrength;
      this.personalizationString = personalizationString;
      this.predictionResistance = predictionResistance;
    }
  }

  // HMAC_DRBG 支持的 MAC 算法
  public static final Map<String, Integer> SUPPORTED_MAC_ALGORITHMS =
      new HashMap<>() {{
        put("HMAC-SHA1", 160);
        put("HMAC-SHA224", 224);
        put("HMAC-SHA256", 256);
        put("HMAC-SHA384", 384);
        put("HMAC-SHA512", 512);
      }};
}

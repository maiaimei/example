package org.example.security.drbg;

import java.util.HashMap;
import java.util.Map;

// CTR_DRBG 实现
public class CTRDRBGExample {

  public static class CTRDRBGConfig {

    private final String blockCipher; // AES-128, AES-192, AES-256
    private final int securityStrength;
    private final byte[] personalizationString;
    private final boolean useDerivationFunction;
    private final boolean predictionResistance;

    public CTRDRBGConfig(String blockCipher, int securityStrength,
        byte[] personalizationString, boolean useDerivationFunction,
        boolean predictionResistance) {
      this.blockCipher = blockCipher;
      this.securityStrength = securityStrength;
      this.personalizationString = personalizationString;
      this.useDerivationFunction = useDerivationFunction;
      this.predictionResistance = predictionResistance;
    }
  }

  // CTR_DRBG 支持的分组密码
  public static final Map<String, Integer> SUPPORTED_BLOCK_CIPHERS =
      new HashMap<>() {{
        put("AES-128", 128);
        put("AES-192", 192);
        put("AES-256", 256);
      }};
}

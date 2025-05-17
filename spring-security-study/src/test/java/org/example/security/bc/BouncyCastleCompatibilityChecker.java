package org.example.security.bc;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BouncyCastleCompatibilityChecker {

  public static void checkAlgorithmSupport() {
    String[] algorithms = {
        BouncyCastleConstants.ALGORITHM_AES_GCM_NOPADDING,
        BouncyCastleConstants.ALGORITHM_CHACHA20_POLY1305,
        BouncyCastleConstants.ALGORITHM_ED25519,
        BouncyCastleConstants.ALGORITHM_X25519,
        BouncyCastleConstants.ALGORITHM_DRBG_CTR
    };

    for (String algorithm : algorithms) {
      try {
        if (algorithm.startsWith(BouncyCastleConstants.ALGORITHM_AES)) {
          Cipher.getInstance(algorithm, BouncyCastleConstants.PROVIDER_NAME);
        } else {
          SecureRandom.getInstance(algorithm, BouncyCastleConstants.PROVIDER_NAME);
        }
        log.info("Algorithm {} is supported", algorithm);
      } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
        log.error("Algorithm {} is not supported: {}",
            algorithm, e.getMessage());
      }
    }
  }
}

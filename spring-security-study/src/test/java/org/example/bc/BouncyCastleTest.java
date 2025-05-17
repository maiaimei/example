package org.example.bc;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BouncyCastleTest {
  
  @Test
  public void performModernCryptoOperation() {
    try {
      SecureRandom random = SecureRandom.getInstance(BouncyCastleConstants.ALGORITHM_DRBG_CTR, BouncyCastleConstants.PROVIDER_NAME);
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(BouncyCastleConstants.ALGORITHM_ED25519,
          BouncyCastleConstants.PROVIDER_NAME);

      keyGen.initialize(255, random);
      System.out.println(Arrays.toString(keyGen.generateKeyPair().getPublic().getEncoded()));

    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new RuntimeException("Modern crypto operations not supported", e);
    }
  }

}

package org.example.security;

import java.io.Serial;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SecureCTRDRBGRandom extends SecureRandom {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final String ALGORITHM = "CTR-DRBG";
  private final CTRSP800DRBG drbg;

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  public SecureCTRDRBGRandom() throws NoSuchAlgorithmException {
    super(null, new BouncyCastleProvider());

    // Initialize base SecureRandom for entropy using SHA1PRNG
    SecureRandom baseRandom = SecureRandom.getInstance("SHA1PRNG");
    baseRandom.setSeed(System.nanoTime());

    // Create entropy source provider and source
    EntropySourceProvider entropySourceProvider =
        new BasicEntropySourceProvider(baseRandom, true);
    EntropySource entropySource = entropySourceProvider.get(256);

    // Initialize CTR_DRBG
    this.drbg = new CTRSP800DRBG(
        new AESLightEngine(),  // AES engine
        256,                   // Security strength
        256,                   // Block size
        entropySource,         // Entropy source
        null,                  // Personalization string
        null                   // Additional input
    );
  }

  @Override
  public String getAlgorithm() {
    return ALGORITHM;
  }

  @Override
  public void nextBytes(byte[] bytes) {
    // Generate random bytes using CTR_DRBG
    drbg.generate(bytes, null, false);
  }


}

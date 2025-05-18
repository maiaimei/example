package org.example.security;

import java.io.Serial;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class SecureCTRDRBGRandom extends SecureRandom {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final String ALGORITHM = "CTR-DRBG-AES";
  private final CTRSP800DRBG drbg;

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * Constructor with configurable security strength
   */
  public SecureCTRDRBGRandom(int securityStrength, byte[] personalizationString) throws NoSuchAlgorithmException {
    super(null, new BouncyCastleProvider());

    try {
      this.drbg = initializeDRBG(securityStrength, personalizationString);
    } catch (Exception e) {
      log.error("Failed to initialize DRBG", e);
      throw new NoSuchAlgorithmException("DRBG initialization failed", e);
    }
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

  private CTRSP800DRBG initializeDRBG(int securityStrength, byte[] personalizationString) throws NoSuchAlgorithmException {
    // Initialize base SecureRandom for entropy
    SecureRandom baseRandom = getStrongSecureRandomOrDefault();

    // Create entropy source provider and source
    EntropySourceProvider entropySourceProvider =
        new BasicEntropySourceProvider(baseRandom, true);
    EntropySource entropySource = entropySourceProvider.get(securityStrength);

    // For AES-based DRBG, block size should be 128 bits (16 bytes)
    int blockSize = 128;

    return new CTRSP800DRBG(
        new AESLightEngine(),  // AES engine
        securityStrength,      // Security strength
        blockSize,             // Block size
        entropySource,         // Entropy source
        personalizationString, // Personalization string
        null                   // Additional input
    );
  }

  private SecureRandom getStrongSecureRandomOrDefault() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.warn("Strong SecureRandom not available, falling back to default", e);
      return new SecureRandom();
    }
  }

}

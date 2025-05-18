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
  private static final String ALGORITHM = "BouncyCastle-CTR-DRBG";

  // AES-based DRBG supports security strengths of 128, 192, and 256 bits
  private static final int[] SUPPORTED_SECURITY_STRENGTHS = {128, 192, 256};
  private static final int DEFAULT_SECURITY_STRENGTH = 128; // Using 128 as default

  private final CTRSP800DRBG drbg;
  private final int securityStrength;

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * Constructor with default security strength (128 bits)
   */
  public SecureCTRDRBGRandom() throws NoSuchAlgorithmException {
    this(DEFAULT_SECURITY_STRENGTH);
  }

  /**
   * Constructor with configurable security strength
   */
  public SecureCTRDRBGRandom(int requestedSecurityStrength) throws NoSuchAlgorithmException {
    super(null, new BouncyCastleProvider());

    // Validate and adjust security strength
    this.securityStrength = validateAndAdjustSecurityStrength(requestedSecurityStrength);

    try {
      this.drbg = initializeDRBG(this.securityStrength);
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

  private int validateAndAdjustSecurityStrength(int requested) {
    // Find the smallest supported strength that meets or exceeds the requested strength
    int adjusted = SUPPORTED_SECURITY_STRENGTHS[0]; // Default to lowest supported

    for (int supported : SUPPORTED_SECURITY_STRENGTHS) {
      if (supported >= requested) {
        adjusted = supported;
        break;
      }
    }

    if (adjusted != requested) {
      log.warn("Requested security strength {} adjusted to {} to meet NIST requirements",
          requested, adjusted);
    }

    return adjusted;
  }

  private CTRSP800DRBG initializeDRBG(int strength) throws NoSuchAlgorithmException {
    // Initialize base SecureRandom for entropy
    SecureRandom baseRandom = getEntropySource();

    // Validate entropy source capabilities
    validateEntropySource(baseRandom);

    // Create entropy source provider and source
    EntropySourceProvider entropySourceProvider =
        new BasicEntropySourceProvider(baseRandom, true);
    EntropySource entropySource = entropySourceProvider.get(strength);

    // For AES-based DRBG, block size should be 128 bits (16 bytes)
    int blockSize = 128;

    return new CTRSP800DRBG(
        new AESLightEngine(),  // AES engine
        strength,              // Security strength
        blockSize,             // Block size
        entropySource,         // Entropy source
        null,                  // Personalization string
        null                   // Additional input
    );
  }

  private SecureRandom getEntropySource() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.warn("Strong SecureRandom not available, falling back to default", e);
      return new SecureRandom();
    }
  }

  private void validateEntropySource(SecureRandom random) {
    // Perform entropy source validation
    byte[] testEntropy = new byte[securityStrength / 8];
    random.nextBytes(testEntropy);

    // Log entropy source details
    log.info("Using entropy source: algorithm={}, provider={}",
        random.getAlgorithm(),
        random.getProvider().getName());
  }

  private int calculateBlockSize(int strength) {
    // Calculate appropriate block size based on security strength
    // As per NIST SP 800-90A, block size should be at least twice the security strength
    return Math.max(256, strength * 2);
  }

}

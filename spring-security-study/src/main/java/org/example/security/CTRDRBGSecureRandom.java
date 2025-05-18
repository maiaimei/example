package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class CTRDRBGSecureRandom extends SecureRandom {

  private static final int SECURITY_STRENGTH = 256; // Security strength in bits
  private static final byte[] DEFAULT_PERSONALIZATION_STRING = "CTR-DRBG-AES256".getBytes();

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  public CTRDRBGSecureRandom() {
      super(new CTRDRBGSecureRandomSpi(), new BouncyCastleProvider());
  }

  public CTRDRBGSecureRandom(byte[] personalizationString) {
    super(new CTRDRBGSecureRandomSpi(personalizationString), new BouncyCastleProvider());
  }

  @Override
  public String getAlgorithm() {
    return "CTR-DRBG-" + SECURITY_STRENGTH + "-AES";
  }

  private static class CTRDRBGSecureRandomSpi extends SecureRandomSpi {
    
    private static final int BLOCK_SIZE = 128;        // AES的块大小

    /**
     * Entropy bits (must be >= security strength, must be at least 1.5 times the security strength.)
     */
    private static final int ENTROPY_BITS = 384;

    private final CTRSP800DRBG drbg;

    public CTRDRBGSecureRandomSpi() {
      this(DEFAULT_PERSONALIZATION_STRING);
    }

    public CTRDRBGSecureRandomSpi(byte[] personalizationString) {
      // Create an entropy source provider
      EntropySourceProvider entropySourceProvider = new CTRDRBGEntropySourceProvider(getStrongOrDefaultSecureRandom(), true);

      // Create an entropy source
      EntropySource entropySource = entropySourceProvider.get(ENTROPY_BITS);

      // Initialize the DRBG with AES-256
      drbg = new CTRSP800DRBG(
          AESEngine.newInstance(),
          SECURITY_STRENGTH,
          BLOCK_SIZE,
          entropySource,
          personalizationString,
          null // No additional input
      );
    }

    @Override
    protected void engineSetSeed(byte[] seed) {
      // CTR-DRBG does not require external seeding
    }

    @Override
    protected void engineNextBytes(byte[] bytes) {
      int result = drbg.generate(bytes, null, false);
      if (result < 0) {
        throw new IllegalStateException("CTR-DRBG failed to generate random bytes");
      }
    }

    @Override
    protected byte[] engineGenerateSeed(int numBytes) {
      byte[] seed = new byte[numBytes];
      engineNextBytes(seed);
      return seed;
    }

    private SecureRandom getStrongOrDefaultSecureRandom() {
      try {
        return SecureRandom.getInstanceStrong();
      } catch (NoSuchAlgorithmException e) {
        log.warn("Strong SecureRandom not available, falling back to default", e);
        return new SecureRandom();
      }
    }
  }

  private static class CTRDRBGEntropySourceProvider implements EntropySourceProvider {

    private final SecureRandom secureRandom;
    private final boolean predictionResistant;

    public CTRDRBGEntropySourceProvider(SecureRandom secureRandom, boolean predictionResistant) {
      this.secureRandom = secureRandom;
      this.predictionResistant = predictionResistant;
    }

    @Override
    public EntropySource get(final int bitsRequired) {
      return new EntropySource() {
        @Override
        public boolean isPredictionResistant() {
          return predictionResistant;
        }

        @Override
        public byte[] getEntropy() {
          byte[] entropy = new byte[(bitsRequired + 7) / 8];
          secureRandom.nextBytes(entropy);
          return entropy;
        }

        @Override
        public int entropySize() {
          return bitsRequired;
        }
      };
    }
  }

}
package org.example.security;

import java.io.Serial;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CTRDRBGSecureRandom extends SecureRandom {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final String ALGORITHM = "CTR-DRBG-AES256";

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  public CTRDRBGSecureRandom() {
    super(new CTRDRBGSpi(), new BouncyCastleProvider());
  }

  @Override
  public String getAlgorithm() {
    return ALGORITHM;
  }

  private static class CTRDRBGSpi extends SecureRandomSpi {

    private static final int SECURITY_STRENGTH = 256; // Security strength in bits
    private static final int BLOCK_SIZE = 128; // AES的块大小
    private static final int ENTROPY_BITS = 384; // Entropy bits (must be >= security strength)
    private static final byte[] PERSONALIZATION_STRING = "CTR-DRBG-AES256".getBytes();

    private final CTRSP800DRBG drbg;

    public CTRDRBGSpi() {
      // Create an entropy source provider
      EntropySourceProvider entropySourceProvider = new DefaultEntropySourceProvider(new SecureRandom(), true);

      // Create an entropy source
      EntropySource entropySource = entropySourceProvider.get(ENTROPY_BITS);

      // Initialize the DRBG with AES-256
      drbg = new CTRSP800DRBG(
          AESEngine.newInstance(),
          SECURITY_STRENGTH,
          BLOCK_SIZE,
          entropySource,
          PERSONALIZATION_STRING,
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
  }

  private static class DefaultEntropySourceProvider implements EntropySourceProvider {

    private final SecureRandom secureRandom;
    private final boolean predictionResistant;

    public DefaultEntropySourceProvider(SecureRandom secureRandom, boolean predictionResistant) {
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
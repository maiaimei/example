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

/**
 * A custom implementation of SecureRandom using CTR-DRBG (Counter mode Deterministic Random Bit Generator) with AES-256 for
 * cryptographic randomness.
 */
@Slf4j
public class CTRDRBGSecureRandom extends SecureRandom {

  // Security strength in bits
  private static final int SECURITY_STRENGTH = 256;

  // The default personalization string.
  private static final byte[] DEFAULT_PERSONALIZATION_STRING = "CTR-DRBG-AES256".getBytes();

  // Static block to register the BouncyCastle provider if not already registered
  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * Default constructor that initializes the SecureRandom with a default
   * personalization string.
   */
  public CTRDRBGSecureRandom() {
    super(new CTRDRBGSecureRandomSpi(), new BouncyCastleProvider());
  }

  /**
   * Constructor that allows specifying a custom personalization string.
   *
   * @param personalizationString A byte array used to personalize the DRBG
   *                              instance.
   */
  public CTRDRBGSecureRandom(byte[] personalizationString) {
    super(new CTRDRBGSecureRandomSpi(personalizationString), new BouncyCastleProvider());
  }

  /**
   * Returns the algorithm name used by this SecureRandom implementation.
   *
   * @return The algorithm name.
   */
  @Override
  public String getAlgorithm() {
    return "CTR-DRBG-" + SECURITY_STRENGTH + "-AES";
  }

  /**
   * A custom implementation of SecureRandomSpi that uses CTR-DRBG with AES-256.
   */
  private static class CTRDRBGSecureRandomSpi extends SecureRandomSpi {

    // AES block size in bits
    private static final int BLOCK_SIZE = 128;

    // Entropy bits (must be >= security strength)
    private static final int ENTROPY_BITS = 384;

    // The DRBG instance
    private final CTRSP800DRBG drbg;

    /**
     * Default constructor that initializes the DRBG with the default personalization string.
     */
    public CTRDRBGSecureRandomSpi() {
      this(DEFAULT_PERSONALIZATION_STRING);
    }

    /**
     * Constructor that initializes the DRBG with a custom personalization string.
     *
     * @param personalizationString A byte array used to personalize the DRBG
     *                              instance.
     */
    public CTRDRBGSecureRandomSpi(byte[] personalizationString) {
      // Create an entropy source provider
      EntropySourceProvider entropySourceProvider = new CTRDRBGEntropySourceProvider(getStrongOrDefaultSecureRandom(),
          true);

      // Create an entropy source
      EntropySource entropySource = entropySourceProvider.get(ENTROPY_BITS);

      // Initialize the DRBG with AES-256
      drbg = new CTRSP800DRBG(
          AESEngine.newInstance(), // AES engine
          SECURITY_STRENGTH, // Security strength in bits
          BLOCK_SIZE, // Block size in bits
          entropySource, // Entropy source
          personalizationString, // Personalization string
          null // No additional input
      );
    }

    /**
     * This method is not used because CTR-DRBG does not require external seeding.
     *
     * @param seed The seed bytes (ignored).
     */
    @Override
    protected void engineSetSeed(byte[] seed) {
      // CTR-DRBG does not require external seeding
    }

    /**
     * Generates the specified number of random bytes and fills the provided byte
     * array.
     *
     * @param bytes The byte array to fill with random bytes.
     */
    @Override
    protected void engineNextBytes(byte[] bytes) {
      int result = drbg.generate(bytes, null, false);
      if (result < 0) {
        throw new IllegalStateException("CTR-DRBG failed to generate random bytes. Error code: " + result);
      }
    }

    /**
     * Generates a seed of the specified length.
     *
     * @param numBytes The number of bytes for the seed.
     * @return A byte array containing the generated seed.
     */
    @Override
    protected byte[] engineGenerateSeed(int numBytes) {
      byte[] seed = new byte[numBytes];
      engineNextBytes(seed);
      return seed;
    }

    /**
     * Returns a strong SecureRandom instance if available, otherwise falls back to
     * the default implementation.
     *
     * @return A SecureRandom instance.
     */
    private SecureRandom getStrongOrDefaultSecureRandom() {
      try {
        return SecureRandom.getInstanceStrong();
      } catch (NoSuchAlgorithmException e) {
        log.warn("Strong SecureRandom not available, falling back to default", e);
        return new SecureRandom();
      }
    }
  }

  /**
   * A custom implementation of EntropySourceProvider that uses a SecureRandom
   * instance
   * to provide entropy for the DRBG.
   */
  private static class CTRDRBGEntropySourceProvider implements EntropySourceProvider {

    // The underlying SecureRandom instance
    private final SecureRandom secureRandom;

    // Whether the entropy source is prediction-resistant
    private final boolean predictionResistant;

    /**
     * Constructor for the entropy source provider.
     *
     * @param secureRandom        The SecureRandom instance to use for entropy.
     * @param predictionResistant Whether the entropy source is
     *                            prediction-resistant.
     */
    public CTRDRBGEntropySourceProvider(SecureRandom secureRandom, boolean predictionResistant) {
      this.secureRandom = secureRandom;
      this.predictionResistant = predictionResistant;
    }

    /**
     * Returns an EntropySource that provides the required number of entropy bits.
     *
     * @param bitsRequired The number of entropy bits required.
     * @return An EntropySource instance.
     */
    @Override
    public EntropySource get(final int bitsRequired) {
      return new EntropySource() {

        /**
         * Indicates whether the entropy source is prediction-resistant.
         *
         * @return True if prediction-resistant, false otherwise.
         */
        @Override
        public boolean isPredictionResistant() {
          return predictionResistant;
        }

        /**
         * Generates and returns the required amount of entropy.
         *
         * @return A byte array containing the entropy.
         */
        @Override
        public byte[] getEntropy() {
          byte[] entropy = new byte[(bitsRequired + 7) / 8]; // Round up to the nearest byte
          secureRandom.nextBytes(entropy);
          return entropy;
        }

        /**
         * Returns the size of the entropy in bits.
         *
         * @return The number of entropy bits.
         */
        @Override
        public int entropySize() {
          return bitsRequired;
        }
      };
    }
  }

}
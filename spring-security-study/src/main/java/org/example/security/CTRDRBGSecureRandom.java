package org.example.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
  private static final int MAX_BITS_PER_REQUEST = 262144; // 32KB
  private static final int MAX_BYTES_PER_REQUEST = MAX_BITS_PER_REQUEST / 8;

  /**
   * The default personalization string.
   * <p>
   * 默认个性化字符串为 "CTR-DRBG-AES256"，可能在某些场景下不够独特，容易被预测。
   * <p>
   * 建议：允许用户在初始化时提供更随机的个性化字符串，或者动态生成默认值。
   */
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
    this(null);
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
   * 重写nextBytes方法，处理大量数据请求
   */
  @Override
  public void nextBytes(byte[] bytes) {
    if (bytes == null) {
      throw new IllegalArgumentException("Output buffer cannot be null");
    }

    // 如果请求的字节数超过限制，分批处理
    if (bytes.length > MAX_BYTES_PER_REQUEST) {
      int offset = 0;
      while (offset < bytes.length) {
        int length = Math.min(MAX_BYTES_PER_REQUEST, bytes.length - offset);
        byte[] temp = new byte[length];
        super.nextBytes(temp);
        System.arraycopy(temp, 0, bytes, offset, length);
        offset += length;
      }
    } else {
      super.nextBytes(bytes);
    }
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

    /**
     * Entropy bits (must be >= security strength)
     * <p>熵位数的硬编码</p>
     * <p>ENTROPY_BITS 设置为 384 位，虽然满足 AES-256 的安全需求，但硬编码可能限制灵活性。</p>
     * <p>建议：将熵位数作为可配置参数，允许根据需求调整。</p>
     */
    private static final int ENTROPY_BITS = 384;

    /**
     * Maximum number of bytes that can be generated before requiring a reseed
     * <p>
     * MAX_BYTES_BEFORE_RESEED 设置为 1,000,000 字节（约 1MB），可能过于宽松。在高安全性场景下，建议更频繁地重新播种。
     * <p>
     * 建议：根据实际需求调整重新播种的阈值。
     */
    private static final long MAX_BYTES_BEFORE_RESEED = 1_000_000; // Example threshold

    // The DRBG instance
    private final CTRSP800DRBG drbg;

    // Counter to track the number of bytes generated
    private long bytesGenerated = 0;

    // Lock for thread safety
    private final Lock lock = new ReentrantLock();

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
      lock.lock();
      try {
        if (bytes.length > MAX_BYTES_PER_REQUEST) {
          int offset = 0;
          while (offset < bytes.length) {
            int length = Math.min(MAX_BYTES_PER_REQUEST, bytes.length - offset);
            byte[] temp = new byte[length];
            generateNextBytes(temp);
            System.arraycopy(temp, 0, bytes, offset, length);
            offset += length;
          }
        } else {
          generateNextBytes(bytes);
        }
      } finally {
        lock.unlock();
      }
    }

    private void generateNextBytes(byte[] bytes) {
      // Check if reseed is required
      if (bytesGenerated >= MAX_BYTES_BEFORE_RESEED) {
        reseed();
      }

      // Generate random bytes
      int result = drbg.generate(bytes, null, false);
      if (result < 0) {
        log.error("CTR-DRBG failed to generate random bytes. Error code: {}", result);
        throw new IllegalStateException("CTR-DRBG failed to generate random bytes. Error code: " + result);
      }

      // Update the counter
      bytesGenerated += bytes.length;
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
     * Reseeds the DRBG with new entropy from the entropy source.
     */
    private void reseed() {
      log.info("Reseeding CTR-DRBG to ensure continued security and randomness.");
      // Create a new entropy source
      EntropySourceProvider entropySourceProvider = new CTRDRBGEntropySourceProvider(
          getStrongOrDefaultSecureRandom(),
          true);
      EntropySource entropySource = entropySourceProvider.get(ENTROPY_BITS);

      // Reseed the DRBG
      drbg.reseed(entropySource.getEntropy());

      // Reset the counter
      bytesGenerated = 0;
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
   *
   * CTRDRBGEntropySourceProvider 使用了 SecureRandom 作为熵源，但 SecureRandom 的质量依赖于底层实现。如果底层实现的熵源质量较低，可能会影响 DRBG 的安全性。
   * 建议：在生产环境中，确保使用高质量的熵源（如硬件随机数生成器）。
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
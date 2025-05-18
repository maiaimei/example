package org.example.security;

import java.io.Serial;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class SecureCTRDRBGRandom extends SecureRandom {

  @Serial
  private static final long serialVersionUID = 1L;

  // 更新算法名称以反映AES-256
  private static final String ALGORITHM = "CTR-DRBG-AES256";

  // NIST SP 800-90A Rev.1 规定的参数
  private static final int SECURITY_STRENGTH = 256; // AES-256的安全强度
  private static final int BLOCK_SIZE = 128; // AES的块大小
  private static final long RESEED_INTERVAL = 1L << 48; // NIST建议的重新播种间隔
  private static final int MAX_BYTES_PER_REQUEST = 1 << 16; // 每次请求最大字节数
  private static final int MAX_PERSONALIZATION_LENGTH = 235; // 个性化字符串最大长度
  private static final int VALIDATION_SAMPLES = 1000;
  private static final double MIN_ENTROPY_BITS_PER_BYTE = 7.5;
  private static final int MIN_UNIQUE_BYTES_PERCENTAGE = 95;

  private final Object drbgLock = new Object();
  private final CTRSP800DRBG drbg;
  private volatile boolean isReseedRequired;
  private long bytesGeneratedSinceReseed;

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * 使用默认个性化字符串构造器
   */
  public SecureCTRDRBGRandom() throws NoSuchAlgorithmException {
    this(generatePersonalizationString());
  }

  /**
   * 使用指定个性化字符串的构造器
   */
  public SecureCTRDRBGRandom(byte[] personalizationString) throws NoSuchAlgorithmException {
    super(null, new BouncyCastleProvider());

    validatePersonalizationString(personalizationString);

    try {
      this.drbg = initializeDRBG(personalizationString);
      this.bytesGeneratedSinceReseed = 0;
      this.isReseedRequired = false;
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
  public synchronized void nextBytes(byte[] bytes) {
    if (bytes == null) {
      throw new IllegalArgumentException("Output buffer cannot be null");
    }

    if (bytes.length > MAX_BYTES_PER_REQUEST) {
      throw new IllegalArgumentException(
          String.format("Request size %d exceeds maximum allowed size %d",
              bytes.length, MAX_BYTES_PER_REQUEST));
    }

    synchronized (drbgLock) {
      // 检查是否需要重新播种
      if (isReseedRequired ||
          bytesGeneratedSinceReseed + bytes.length > RESEED_INTERVAL) {
        reseedDRBG();
      }

      try {
        // Generate random bytes using CTR_DRBG
        drbg.generate(bytes, null, false);
        bytesGeneratedSinceReseed += bytes.length;
      } catch (IllegalStateException e) {
        log.warn("DRBG indicated reseed requirement during generation", e);
        reseedDRBG();
        drbg.generate(bytes, null, false);
        bytesGeneratedSinceReseed = bytes.length;
      }
    }
  }

  private void reseedDRBG() {
    try {
      byte[] entropyInput = new byte[SECURITY_STRENGTH / 8];
      SecureRandom.getInstanceStrong().nextBytes(entropyInput);

      synchronized (drbgLock) {
        drbg.reseed(entropyInput);
        bytesGeneratedSinceReseed = 0;
        isReseedRequired = false;
        log.debug("DRBG successfully reseeded");
      }
    } catch (NoSuchAlgorithmException e) {
      log.error("Failed to reseed DRBG", e);
      throw new IllegalStateException("DRBG reseed failed", e);
    }
  }

  /**
   * 显式触发DRBG重新播种
   */
  public void reseed() {
    synchronized (drbgLock) {
      reseedDRBG();
    }
  }

  /**
   * 获取自上次重新播种以来生成的字节数
   */
  public long getBytesGeneratedSinceReseed() {
    synchronized (drbgLock) {
      return bytesGeneratedSinceReseed;
    }
  }

  /**
   * 检查是否需要重新播种
   */
  public boolean isReseedRequired() {
    return isReseedRequired ||
        bytesGeneratedSinceReseed >= RESEED_INTERVAL;
  }

  /**
   * 清理敏感数据
   */
  public void destroy() {
    synchronized (drbgLock) {
      bytesGeneratedSinceReseed = 0;
      isReseedRequired = true;
    }
  }

  private static byte[] generatePersonalizationString() {
    try {
      byte[] personalization = new byte[32]; // 256 bits
      SecureRandom.getInstanceStrong().nextBytes(personalization);
      return personalization;
    } catch (NoSuchAlgorithmException e) {
      log.warn("Failed to generate strong personalization string, using default SecureRandom");
      byte[] personalization = new byte[32];
      new SecureRandom().nextBytes(personalization);
      return personalization;
    }
  }

  private void validatePersonalizationString(byte[] personalization) {
    if (personalization != null && personalization.length > MAX_PERSONALIZATION_LENGTH) {
      throw new IllegalArgumentException(
          String.format("Personalization string length %d exceeds maximum allowed length %d",
              personalization.length, MAX_PERSONALIZATION_LENGTH));
    }
  }

  private CTRSP800DRBG initializeDRBG(byte[] personalizationString)
      throws NoSuchAlgorithmException {
    // 获取强熵源
    SecureRandom entropySource = getStrongEntropySource();

    // 验证熵源
    validateEntropySource(entropySource);

    // 创建熵源提供者
    EntropySourceProvider entropySourceProvider =
        new BasicEntropySourceProvider(entropySource, true);
    EntropySource source = entropySourceProvider.get(SECURITY_STRENGTH);

    // 使用AESEngine替代AESLightEngine以获得更好的性能
    return new CTRSP800DRBG(
        AESEngine.newInstance(),  // AES引擎
        SECURITY_STRENGTH,        // 安全强度 (256位)
        BLOCK_SIZE,              // 块大小 (128位)
        source,                  // 熵源
        personalizationString,   // 个性化字符串
        null                    // 额外输入
    );
  }

  private SecureRandom getStrongEntropySource() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.warn("Strong SecureRandom not available, falling back to default", e);
      return new SecureRandom();
    }
  }

  private void validateEntropySource(SecureRandom random) throws NoSuchAlgorithmException {
    int sampleSize = SECURITY_STRENGTH / 8;
    byte[][] samples = new byte[VALIDATION_SAMPLES][sampleSize];

    try {
      // Collect multiple samples for entropy analysis
      for (int i = 0; i < VALIDATION_SAMPLES; i++) {
        random.nextBytes(samples[i]);
      }

      // Perform entropy source validation checks
      validateEntropyRate(samples, sampleSize);
      validateUniqueness(samples, sampleSize);
      validateDistribution(samples, sampleSize);
      validateSequentialCorrelation(samples, sampleSize);

      // Log entropy source details
      log.info("Entropy source validation successful: algorithm={}, provider={}, security strength={}",
          random.getAlgorithm(),
          random.getProvider().getName(),
          SECURITY_STRENGTH);

    } catch (EntropyValidationException e) {
      log.error("Entropy source validation failed", e);
      throw new NoSuchAlgorithmException("Insufficient entropy source quality: " + e.getMessage());
    } finally {
      // Secure cleanup of samples
      for (byte[] sample : samples) {
        Arrays.fill(sample, (byte) 0);
      }
    }
  }

  private void validateEntropyRate(byte[][] samples, int sampleSize) throws EntropyValidationException {
    // Calculate empirical entropy using frequency analysis
    int[] frequencies = new int[256];
    long totalBytes = (long) samples.length * sampleSize;

    for (byte[] sample : samples) {
      for (byte b : sample) {
        frequencies[b & 0xFF]++;
      }
    }

    double empiricalEntropy = calculateShannonEntropy(frequencies, totalBytes);
    double entropyBitsPerByte = empiricalEntropy / Math.log(2);

    if (entropyBitsPerByte < MIN_ENTROPY_BITS_PER_BYTE) {
      throw new EntropyValidationException(
          String.format("Insufficient entropy rate: %.2f bits/byte (minimum required: %.2f)",
              entropyBitsPerByte, MIN_ENTROPY_BITS_PER_BYTE));
    }
  }

  private double calculateShannonEntropy(int[] frequencies, long totalBytes) {
    double entropy = 0.0;
    for (int frequency : frequencies) {
      if (frequency > 0) {
        double probability = (double) frequency / totalBytes;
        entropy -= probability * Math.log(probability);
      }
    }
    return entropy;
  }

  private void validateUniqueness(byte[][] samples, int sampleSize) throws EntropyValidationException {
    Set<ByteBuffer> uniqueSamples = new HashSet<>();
    for (byte[] sample : samples) {
      uniqueSamples.add(ByteBuffer.wrap(sample.clone()));
    }

    int uniquePercentage = (uniqueSamples.size() * 100) / samples.length;
    if (uniquePercentage < MIN_UNIQUE_BYTES_PERCENTAGE) {
      throw new EntropyValidationException(
          String.format("Insufficient uniqueness: %d%% unique samples (minimum required: %d%%)",
              uniquePercentage, MIN_UNIQUE_BYTES_PERCENTAGE));
    }
  }

  private void validateDistribution(byte[][] samples, int sampleSize) throws EntropyValidationException {
    // Perform chi-square test for uniform distribution
    int[] observed = new int[256];
    long totalBytes = (long) samples.length * sampleSize;
    double expectedFrequency = totalBytes / 256.0;

    for (byte[] sample : samples) {
      for (byte b : sample) {
        observed[b & 0xFF]++;
      }
    }

    double chiSquare = 0.0;
    for (int frequency : observed) {
      double difference = frequency - expectedFrequency;
      chiSquare += (difference * difference) / expectedFrequency;
    }

    // Critical value for 255 degrees of freedom at 0.001 significance level
    double criticalValue = 327.86;
    if (chiSquare > criticalValue) {
      throw new EntropyValidationException(
          String.format("Distribution test failed: chi-square = %.2f (critical value: %.2f)",
              chiSquare, criticalValue));
    }
  }

  private void validateSequentialCorrelation(byte[][] samples, int sampleSize)
      throws EntropyValidationException {
    // Calculate serial correlation coefficient
    double correlation = calculateSerialCorrelation(samples, sampleSize);
    double maxAllowedCorrelation = 0.1; // Maximum allowed correlation coefficient

    if (Math.abs(correlation) > maxAllowedCorrelation) {
      throw new EntropyValidationException(
          String.format("High sequential correlation detected: %.4f (maximum allowed: %.4f)",
              Math.abs(correlation), maxAllowedCorrelation));
    }
  }

  private double calculateSerialCorrelation(byte[][] samples, int sampleSize) {
    long n = (long) samples.length * sampleSize;
    double sum = 0.0;
    double sumSquared = 0.0;
    double sumProduct = 0.0;

    for (byte[] sample : samples) {
      for (int i = 0; i < sample.length - 1; i++) {
        int current = sample[i] & 0xFF;
        int next = sample[i + 1] & 0xFF;

        sum += current;
        sumSquared += current * current;
        sumProduct += current * next;
      }
    }

    double numerator = (n - 1) * sumProduct - sum * sum;
    double denominator = (n - 1) * sumSquared - sum * sum;

    return denominator == 0 ? 0 : numerator / denominator;
  }

  private static class EntropyValidationException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntropyValidationException(String message) {
      super(message);
    }
  }
}

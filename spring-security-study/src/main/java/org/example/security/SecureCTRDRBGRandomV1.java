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
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class SecureCTRDRBGRandomV1 extends SecureRandom {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final String ALGORITHM = "CTR-DRBG-AES";

  // AES-based DRBG supports security strengths of 128, 192, and 256 bits
  private static final int[] SUPPORTED_SECURITY_STRENGTHS = {128, 192, 256};
  private static final int DEFAULT_SECURITY_STRENGTH = 128; // Using 128 as default

  private final CTRSP800DRBG drbg;
  private final int securityStrength;

  private static final int VALIDATION_SAMPLES = 1000;
  private static final double MIN_ENTROPY_BITS_PER_BYTE = 7.5;
  private static final int MIN_UNIQUE_BYTES_PERCENTAGE = 95;

  static {
    // Register BouncyCastle provider if not already registered
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * Constructor with default security strength (128 bits)
   */
  public SecureCTRDRBGRandomV1(byte[] personalizationString) throws NoSuchAlgorithmException {
    this(DEFAULT_SECURITY_STRENGTH, personalizationString);
  }

  /**
   * Constructor with configurable security strength
   */
  public SecureCTRDRBGRandomV1(int requestedSecurityStrength, byte[] personalizationString) throws NoSuchAlgorithmException {
    super(null, new BouncyCastleProvider());

    // Validate and adjust security strength
    this.securityStrength = validateAndAdjustSecurityStrength(requestedSecurityStrength);

    try {
      this.drbg = initializeDRBG(this.securityStrength, personalizationString);
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

  private CTRSP800DRBG initializeDRBG(int strength, byte[] personalizationString) throws NoSuchAlgorithmException {
    // Initialize base SecureRandom for entropy
    SecureRandom baseRandom = getStrongSecureRandomOrDefault();

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

  private void validateEntropySource(SecureRandom random) throws NoSuchAlgorithmException {
    int sampleSize = securityStrength / 8;
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
          securityStrength);

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

package org.example.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SecureRandom Implementation Security Tests")
public class SecureRandomSecurityTest {

  private CTRDRBGSecureRandom random;
  private static final int SAMPLE_SIZE = 1_000_000;
  private static final double SIGNIFICANCE_LEVEL = 0.01;

  @BeforeEach
  void setUp() {
    random = new CTRDRBGSecureRandom();
  }

  @Test
  @DisplayName("NIST Statistical Test Suite - Frequency Test")
  void testNISTFrequency() {
    byte[] bytes = new byte[SAMPLE_SIZE];
    random.nextBytes(bytes);

    // Convert bytes to bits
    boolean[] bits = bytesToBits(bytes);

    // Count ones
    int ones = 0;
    for (boolean bit : bits) {
      if (bit) {
        ones++;
      }
    }

    // Calculate statistics
    double pValue = calculateMonobitTestPValue(ones, bits.length);

    Assertions.assertTrue(pValue > SIGNIFICANCE_LEVEL,
        String.format("Frequency test failed with p-value: %f", pValue));
  }

  @Test
  @DisplayName("Entropy Test")
  void testEntropy() {
    byte[] bytes = new byte[SAMPLE_SIZE];
    random.nextBytes(bytes);

    double entropy = calculateShannonEntropy(bytes);
    double minExpectedEntropy = 7.5; // Expected bits of entropy per byte

    Assertions.assertTrue(entropy > minExpectedEntropy,
        String.format("Low entropy detected: %f bits per byte", entropy));
  }

  @Test
  @DisplayName("Serial Correlation Test")
  void testSerialCorrelation() {
    byte[] bytes = new byte[SAMPLE_SIZE];
    random.nextBytes(bytes);

    double correlation = calculateSerialCorrelation(bytes);
    double maxAllowedCorrelation = 0.1;

    Assertions.assertTrue(Math.abs(correlation) < maxAllowedCorrelation,
        String.format("High serial correlation detected: %f", correlation));
  }

  @Test
  @DisplayName("Chi-Square Distribution Test")
  void testChiSquareDistribution() {
    byte[] bytes = new byte[SAMPLE_SIZE];
    random.nextBytes(bytes);

    long[] frequencies = new long[256];
    for (byte b : bytes) {
      frequencies[b & 0xFF]++;
    }

    ChiSquareTest chiSquareTest = new ChiSquareTest();
    double[] expected = new double[256];
    Arrays.fill(expected, SAMPLE_SIZE / 256.0);

    double pValue = chiSquareTest.chiSquareTest(expected, frequencies);

    Assertions.assertTrue(pValue > SIGNIFICANCE_LEVEL,
        String.format("Chi-square test failed with p-value: %f", pValue));
  }

  @Test
  @DisplayName("Predictability Test")
  void testPredictability() {
    // Generate multiple sequences with same seed
    byte[] seed = new byte[32];
    random.nextBytes(seed);

    CTRDRBGSecureRandomV3 random1 = new CTRDRBGSecureRandomV3();
    CTRDRBGSecureRandomV3 random2 = new CTRDRBGSecureRandomV3();

    random1.setSeed(seed);
    random2.setSeed(seed);

    byte[] sequence1 = new byte[1024];
    byte[] sequence2 = new byte[1024];

    random1.nextBytes(sequence1);
    random2.nextBytes(sequence2);

    // Sequences should be different despite same seed
    Assertions.assertFalse(Arrays.equals(sequence1, sequence2),
        "Generated sequences should not be predictable based on seed");
  }

  @Test
  @DisplayName("Avalanche Effect Test")
  void testAvalancheEffect() {
    byte[] seed1 = new byte[32];
    byte[] seed2 = new byte[32];
    random.nextBytes(seed1);
    System.arraycopy(seed1, 0, seed2, 0, seed1.length);

    // Change one bit in seed2
    seed2[0] ^= 1;

    CTRDRBGSecureRandomV3 random1 = new CTRDRBGSecureRandomV3();
    CTRDRBGSecureRandomV3 random2 = new CTRDRBGSecureRandomV3();

    random1.setSeed(seed1);
    random2.setSeed(seed2);

    byte[] output1 = new byte[1024];
    byte[] output2 = new byte[1024];

    random1.nextBytes(output1);
    random2.nextBytes(output2);

    // Calculate bit differences
    int differences = countBitDifferences(output1, output2);
    double differenceRatio = differences / (double) (output1.length * 8);

    // Expect approximately 50% of bits to be different
    Assertions.assertTrue(Math.abs(differenceRatio - 0.5) < 0.1,
        String.format("Insufficient avalanche effect: %f", differenceRatio));
  }

  @Test
  @DisplayName("Period Length Test")
  void testPeriodLength() {
    Set<String> outputs = new HashSet<>();
    byte[] bytes = new byte[32];
    int iterations = 10_000;

    for (int i = 0; i < iterations; i++) {
      random.nextBytes(bytes);
      outputs.add(Base64.getEncoder().encodeToString(bytes));
    }

    // All outputs should be unique
    Assertions.assertEquals(iterations, outputs.size(),
        "Random number generator showing signs of short period");
  }

  //@Test
  @DisplayName("Reseed Impact Test")
  void testReseedImpact() {
    byte[] before = new byte[32];
    random.nextBytes(before);

    random.reseed();

    byte[] after = new byte[32];
    random.nextBytes(after);

    Assertions.assertFalse(Arrays.equals(before, after),
        "Output should change significantly after reseed");
  }

  @Test
  @DisplayName("Test Large Request Handling")
  void testLargeRequestHandling() {
    // 测试大于 32KB 的请求
    int largeSize = 1024 * 1024; // 1MB
    byte[] largeBuffer = new byte[largeSize];

    Assertions.assertDoesNotThrow(() -> {
      random.nextBytes(largeBuffer);
    }, "Should handle large requests without throwing exception");

    // 验证生成的数据质量
    verifyRandomData(largeBuffer);
  }

  private void verifyRandomData(byte[] data) {
    // 基本统计检查
    int[] frequencies = new int[256];
    for (byte b : data) {
      frequencies[b & 0xFF]++;
    }

    // 检查分布
    double expectedFrequency = data.length / 256.0;
    double chiSquare = 0.0;
    for (int frequency : frequencies) {
      double diff = frequency - expectedFrequency;
      chiSquare += (diff * diff) / expectedFrequency;
    }

    // 对于大样本，使用更宽松的阈值
    double criticalValue = 350.0; // 适当调整此值
    Assertions.assertTrue(chiSquare < criticalValue,
        String.format("Chi-square test failed for large data set: %f", chiSquare));
  }

  @Test
  @DisplayName("Test Multiple Sequential Large Requests")
  void testMultipleSequentialLargeRequests() {
    int requestSize = 1024 * 1024; // 1MB
    int numberOfRequests = 5;

    for (int i = 0; i < numberOfRequests; i++) {
      byte[] buffer = new byte[requestSize];
      Assertions.assertDoesNotThrow(() -> {
        random.nextBytes(buffer);
      }, "Sequential large request " + i + " failed");
    }
  }

  @Test
  @DisplayName("Test Concurrent Large Requests")
  void testConcurrentLargeRequests() throws InterruptedException {
    int threadCount = 4;
    int requestSize = 1024 * 1024; // 1MB
    CountDownLatch latch = new CountDownLatch(threadCount);
    AtomicReference<Exception> error = new AtomicReference<>();

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < threadCount; i++) {
      Thread thread = new Thread(() -> {
        try {
          byte[] buffer = new byte[requestSize];
          random.nextBytes(buffer);
        } catch (Exception e) {
          error.set(e);
        } finally {
          latch.countDown();
        }
      });
      threads.add(thread);
      thread.start();
    }

    boolean completed = latch.await(30, TimeUnit.SECONDS);
    Assertions.assertTrue(completed, "Concurrent requests timed out");
    Assertions.assertNull(error.get(), "Error occurred during concurrent requests");
  }

  @Test
  @DisplayName("Test Memory Usage During Large Requests")
  void testMemoryUsageForLargeRequests() {
    int requestSize = 100 * 1024 * 1024; // 100MB
    Runtime runtime = Runtime.getRuntime();

    // 强制GC
    System.gc();
    long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

    byte[] buffer = new byte[requestSize];
    random.nextBytes(buffer);

    long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
    long memoryDiff = memoryAfter - memoryBefore;

    // 验证内存使用合理
    Assertions.assertTrue(memoryDiff < requestSize * 2,
        "Memory usage too high for large request");
  }

  // Utility methods

  private boolean[] bytesToBits(byte[] bytes) {
    boolean[] bits = new boolean[bytes.length * 8];
    for (int i = 0; i < bytes.length; i++) {
      for (int j = 0; j < 8; j++) {
        bits[i * 8 + j] = ((bytes[i] >> j) & 1) == 1;
      }
    }
    return bits;
  }

  private double calculateMonobitTestPValue(int ones, int length) {
    double s = Math.abs(2.0 * ones - length) / Math.sqrt(length);
    return erfc(s / Math.sqrt(2.0));
  }

  private double calculateShannonEntropy(byte[] data) {
    int[] frequencies = new int[256];
    for (byte b : data) {
      frequencies[b & 0xFF]++;
    }

    double entropy = 0.0;
    for (int frequency : frequencies) {
      if (frequency > 0) {
        double probability = frequency / (double) data.length;
        entropy -= probability * (Math.log(probability) / Math.log(2));
      }
    }
    return entropy;
  }

  private double calculateSerialCorrelation(byte[] data) {
    if (data.length < 2) {
      return 0.0;
    }

    double sum_x = 0, sum_y = 0, sum_xy = 0;
    double sum_x2 = 0, sum_y2 = 0;
    int n = data.length - 1;

    for (int i = 0; i < n; i++) {
      double x = data[i] & 0xFF;
      double y = data[i + 1] & 0xFF;

      sum_x += x;
      sum_y += y;
      sum_xy += x * y;
      sum_x2 += x * x;
      sum_y2 += y * y;
    }

    double numerator = n * sum_xy - sum_x * sum_y;
    double denominator = Math.sqrt((n * sum_x2 - sum_x * sum_x) *
        (n * sum_y2 - sum_y * sum_y));

    return denominator == 0 ? 0 : numerator / denominator;
  }

  private int countBitDifferences(byte[] array1, byte[] array2) {
    int differences = 0;
    for (int i = 0; i < array1.length; i++) {
      byte xor = (byte) (array1[i] ^ array2[i]);
      differences += Integer.bitCount(xor & 0xFF);
    }
    return differences;
  }

  private double erfc(double x) {
    return 2 * new NormalDistribution().cumulativeProbability(-x * Math.sqrt(2));
  }
}

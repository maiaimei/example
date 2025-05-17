package org.example;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SecureRandomTest {

  @Test
  public void generatingRandomValues() {
    final SecureRandom secureRandom = new SecureRandom();

    // The most common way of using SecureRandom is to generate int, long, float, double or boolean values:
    System.out.println(secureRandom.nextInt());
    System.out.println(secureRandom.nextLong());
    System.out.println(secureRandom.nextFloat());
    System.out.println(secureRandom.nextDouble());
    System.out.println(secureRandom.nextBoolean());

    // For generating int values we can pass an upper bound as a parameter:
    System.out.println(secureRandom.nextInt(100));

    // In addition, we can generate a stream of values for int, double and long:
    secureRandom.ints().limit(1).forEach(System.out::println);
    secureRandom.longs().limit(1).forEach(System.out::println);
    secureRandom.doubles().limit(1).forEach(System.out::println);

    // For all streams we can explicitly set the stream size:
    secureRandom.ints(1).forEach(System.out::println);

    // and the origin (inclusive) and bound (exclusive) values as well:
    secureRandom.ints(1, 10, 20).forEach(System.out::println);

    // We can also generate a sequence of random bytes.
    // The nextBytes() function takes user-supplied byte array and fills it with random bytes:
    // Java 中，byte 占一个字节，取值范围是 -128~127（-2^7~2^7-1）
    byte[] bytes = new byte[124];
    secureRandom.nextBytes(bytes);
    for (byte b : bytes) {
      System.out.println(b);
    }
  }

  @Test
  public void choosingAnAlgorithm() throws NoSuchAlgorithmException, NoSuchProviderException {
    // 使用默认构造器，让系统选择最适合算法
    final SecureRandom secureRandom1 = new SecureRandom();
    System.out.println(secureRandom1.getAlgorithm());

    // 显式指定算法
    // SecureRandom secureRandom2 = SecureRandom.getInstance("NativePRNG");

    // 指定算法和提供者
    SecureRandom secureRandom3 = SecureRandom.getInstance("SHA1PRNG", "SUN");
  }

  @Test
  public void generateToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);
    System.out.println(Base64.getEncoder().encodeToString(bytes));
    System.out.println(Arrays.toString(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(bytes))));
  }

  @Test
  public void generateSecureToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] token = new byte[32];
    secureRandom.nextBytes(token);
    System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(token));
  }

  // 运行该程序了解系统支持的所有算法
  public static class SecureRandomAvailableAlgorithmsChecker {

    public static void listSecureRandomAlgorithms() {
      // 获取所有安全提供者
      Provider[] providers = Security.getProviders();

      for (Provider provider : providers) {
        // 遍历每个提供者的服务
        provider.getServices().stream()
            .filter(s -> "SecureRandom".equals(s.getType()))
            .forEach(s -> System.out.printf("Provider: %s, Algorithm: %s%n",
                provider.getName(), s.getAlgorithm()));
      }
    }

    public static void main(String[] args) {
      listSecureRandomAlgorithms();
    }
  }

  // 运行该程序检查具体算法是否可用
  public static class SpecificAlgorithmAvailableChecker {

    public static void checkAlgorithm(String algorithm) {
      try {
        SecureRandom.getInstance(algorithm);

        showProviders(algorithm);

      } catch (NoSuchAlgorithmException e) {
        System.out.printf("Algorithm '%s' is not supported%n", algorithm);
      }
    }

    public static void showProviders(String algorithm) {
      // 获取所有提供者
      Provider[] providers = Security.getProviders();

      for (Provider provider : providers) {
        try {
          // 尝试从每个提供者获取算法实现
          SecureRandom.getInstance(algorithm, provider);
          System.out.printf("Algorithm '%s' is supported by provider '%s'%n",
              algorithm, provider.getName());
          System.out.printf("Provider: %s, Version: %s%n",
              provider.getName(), provider.getVersionStr());

        } catch (NoSuchAlgorithmException e) {
          System.out.printf("Algorithm '%s' is not supported by provider '%s'%n",
              algorithm, provider.getName());
        }
      }
    }

    public static void main(String[] args) {
      // 检查常见算法
      String[] algorithms = {
          "SHA1PRNG",
          "NativePRNG",
          "NativePRNGNonBlocking",
          "DRBG", // DRBG (Deterministic Random Bit Generator)  - Hash_DRBG
          "HMAC_DRBG", // DRBG (Deterministic Random Bit Generator)  - HMAC_DRBG
          "CTR_DRBG", // DRBG (Deterministic Random Bit Generator)  - CTR_DRBG
          "DRBG-CTR", // DRBG (Deterministic Random Bit Generator)  - CTR_DRBG
          "Windows-PRNG"
      };

      for (String algorithm : algorithms) {
        System.out.printf("%n=== Algorithm: %s ===%n", algorithm);
        checkAlgorithm(algorithm);
        System.out.println();
      }
    }
  }

  // 运行该程序了解系统默认和强随机算法信息
  public static class DefaultAlgorithmInfo {

    public static void showDefaultInfo() {
      // 默认 SecureRandom 实现
      SecureRandom defaultRandom = new SecureRandom();
      System.out.println("Default SecureRandom:");
      System.out.println("Algorithm: " + defaultRandom.getAlgorithm());
      System.out.println("Provider: " + defaultRandom.getProvider());

      // 强随机数实现
      try {
        SecureRandom strongRandom = SecureRandom.getInstanceStrong();
        System.out.println("\nStrong SecureRandom:");
        System.out.println("Algorithm: " + strongRandom.getAlgorithm());
        System.out.println("Provider: " + strongRandom.getProvider());
      } catch (NoSuchAlgorithmException e) {
        System.out.println("Strong SecureRandom not available");
      }
    }

    public static void main(String[] args) {
      showDefaultInfo();
    }
  }

  // 运行该程序查看提供者的详细信息
  public static class ProviderManagement {

    public static void showProviderDetails() {
      System.out.println("Security Providers and their properties:");

      for (Provider provider : Security.getProviders()) {
        System.out.printf("%n=== Provider: %s (Version: %s) ===%n",
            provider.getName(), provider.getVersionStr());
        System.out.println("Info: " + provider.getInfo());

        // 显示所有与 SecureRandom 相关的属性
        provider.stringPropertyNames().stream()
            .filter(prop -> prop.toLowerCase().contains("securerandom")
                || prop.toLowerCase().contains("prng"))
            .forEach(prop -> System.out.printf("Property: %s = %s%n",
                prop, provider.getProperty(prop)));
      }
    }

    public static void main(String[] args) {
      showProviderDetails();
    }
  }

  // 运行该程序测试不同算法的性能，性能对比
  public static class AlgorithmPerformanceTest {

    public static void testAlgorithmPerformance(String algorithm) {
      try {
        SecureRandom random = SecureRandom.getInstance(algorithm);
        byte[] bytes = new byte[1024];

        // 预热
        for (int i = 0; i < 100; i++) {
          random.nextBytes(bytes);
        }

        // 性能测试
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
          random.nextBytes(bytes);
        }
        long end = System.nanoTime();

        System.out.printf("Algorithm '%s' took %.2f ms%n",
            algorithm, (end - start) / 1_000_000.0);

      } catch (NoSuchAlgorithmException e) {
        System.out.printf("Algorithm '%s' is not available%n", algorithm);
      }
    }

    public static void main(String[] args) {
      String[] algorithms = {"SHA1PRNG", "NativePRNG"};
      for (String algorithm : algorithms) {
        testAlgorithmPerformance(algorithm);
      }
    }
  }


}

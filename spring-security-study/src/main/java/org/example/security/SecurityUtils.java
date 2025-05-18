package org.example.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 安全工具类
 */
@Slf4j
public class SecurityUtils {

  /**
   * Provider 锁
   */
  private static final Object LOCK = new Object();
  /**
   * 已激活的 Provider 集合
   */
  private static final Set<String> ACTIVE_PROVIDERS = ConcurrentHashMap.newKeySet();

  /**
   * Provider 状态信息
   */
  public static class ProviderState {

    /**
     * Provider 名称
     */
    @Getter
    private final String name;

    /**
     * Provider 是否已安装
     */
    private final boolean isInstalled;

    /**
     * Provider 位置
     */
    @Getter
    private final int position;

    /**
     * Provider 版本
     */
    @Getter
    private final String version;

    /**
     * Provider 支持的算法集合
     */
    @Getter
    private final Set<String> supportedAlgorithms;

    /**
     * 构造函数
     *
     * @param name                Provider 名称
     * @param isInstalled         Provider 是否已安装
     * @param position            Provider 位置
     * @param version             Provider 版本
     * @param supportedAlgorithms Provider 支持的算法集合
     */
    public ProviderState(String name, boolean isInstalled, int position,
        String version, Set<String> supportedAlgorithms) {
      this.name = name;
      this.isInstalled = isInstalled;
      this.position = position;
      this.version = version;
      this.supportedAlgorithms = supportedAlgorithms;
    }

    /**
     * 获取 Provider 是否已安装
     *
     * @return Provider 是否已安装
     */
    public boolean isInstalled() {
      return isInstalled;
    }

    /**
     * 获取 Provider 版本字符串
     *
     * @return Provider 版本字符串
     */
    @Override
    public String toString() {
      return String.format("Provider[name=%s, installed=%b, position=%d, version=%s]",
          name, isInstalled, position, version);
    }
  }

  /**
   * 安全地添加 Provider
   *
   * @param provider Provider 实例
   */
  public static void addProvider(Provider provider) {
    if (provider == null) {
      throw new IllegalArgumentException("Provider cannot be null");
    }
    final String providerName = provider.getName();
    try {
      synchronized (LOCK) {
        if (!ACTIVE_PROVIDERS.contains(providerName)) {
          Security.addProvider(provider);
          ACTIVE_PROVIDERS.add(providerName);
          log.info("Provider {} initialization success,  version: {}, info: {}",
              providerName, provider.getVersionStr(), provider.getInfo());
        }
      }
    } catch (Exception e) {
      log.error("Provider initialization failed", e);
      throw new RuntimeException("Provider initialization failed", e);
    }
  }

  /**
   * 安全地移除 Provider
   *
   * @param providerName Provider 名称
   */
  public static void removeProvider(String providerName) {
    if (providerName == null || providerName.trim().isEmpty()) {
      throw new IllegalArgumentException("Provider name cannot be null or empty");
    }

    synchronized (LOCK) {
      if (ACTIVE_PROVIDERS.contains(providerName)) {
        Security.removeProvider(providerName);
        ACTIVE_PROVIDERS.remove(providerName);
        log.info("Removed provider: {}", providerName);
      }
    }
  }

  /**
   * 检查 Provider 是否存在
   *
   * @param provider Provider 实例
   * @return true 如果 Provider 存在，否则 false
   */
  public static boolean existProvider(Provider provider) {
    return Objects.nonNull(Security.getProvider(provider.getName()));
  }

  /**
   * 检查 Provider 是否存在
   *
   * @param providerName Provider 名称
   * @return true 如果 Provider 存在，否则 false
   */
  public static boolean existProvider(String providerName) {
    return Objects.nonNull(Security.getProvider(providerName));
  }

  /**
   * 获取 Provider 实例
   *
   * @param providerName Provider 名称
   * @return Provider 实例
   */
  public static Provider getProvider(String providerName) {
    final Provider provider = Security.getProvider(providerName);
    if (Objects.isNull(provider)) {
      throw new RuntimeException("Provider " + providerName + " does not exist");
    }
    return provider;
  }

  /**
   * 获取 Provider 支持的算法集合
   *
   * @param provider Provider 实例
   * @return 支持的算法集合
   */
  public static Set<String> getProviderAlgorithms(Provider provider) {
    Set<String> algorithms = new HashSet<>();
    for (Provider.Service service : provider.getServices()) {
      algorithms.add(service.getAlgorithm());
    }
    return algorithms;
  }

  /**
   * 获取 Provider 状态信息
   *
   * @param providerName Provider 名称
   * @return Provider 状态信息
   */
  public static ProviderState getProviderState(String providerName) {
    Provider provider = Security.getProvider(providerName);
    boolean installed = provider != null;
    int position = -1;
    String version = "0.0";
    Set<String> algorithms = new HashSet<>();

    if (installed) {
      Provider[] providers = Security.getProviders();
      for (int i = 0; i < providers.length; i++) {
        if (providers[i].getName().equals(providerName)) {
          position = i + 1;
          break;
        }
      }
      version = provider.getVersionStr();
      algorithms = getProviderAlgorithms(provider);
    }

    return new ProviderState(providerName, installed, position, version, algorithms);
  }

  /**
   * 使用 Provider 执行操作
   *
   * @param provider Provider 实例
   * @param action   操作
   */
  public static void withProvider(Provider provider, Runnable action) {
    String providerName = provider.getName();
    addProvider(provider);
    try {
      if (!existProvider(provider)) {
        throw new RuntimeException("Provider " + providerName + " does not exist");
      }
      action.run();
    } finally {
      removeProvider(providerName);
    }
  }

  /**
   * 打印所有已安装的 Provider 信息
   */
  public static void listInstalledProviders() {
    Provider[] providers = Security.getProviders();
    System.out.println("Installed providers:");
    for (int i = 0; i < providers.length; i++) {
      Provider provider = providers[i];
      System.out.printf("%d. %s (version %s)\n",
          i + 1, provider.getName(), provider.getVersionStr());
    }
  }

  /**
   * 目的：测试消息摘要算法的性能
   * <p>
   * 功能：使用指定的 Provider 和算法进行性能测试
   * <p>
   * 特点：可以指定迭代次数，打印性能测试结果
   * <p>
   * 注意：此方法仅用于性能测试，实际应用中应根据需求选择合适的算法
   *
   * @param providerName 提供者名称
   * @param algorithm    算法名称
   * @param iterations   迭代次数
   */
  public static void performanceTest(String providerName, String algorithm, int iterations) {
    try {
      Provider provider = Security.getProvider(providerName);
      if (provider == null) {
        throw new IllegalArgumentException("Provider not found: " + providerName);
      }

      // 预热（100次迭代）
      MessageDigest digest = MessageDigest.getInstance(algorithm, providerName);
      byte[] data = new byte[1024];
      new SecureRandom().nextBytes(data);
      for (int i = 0; i < 100; i++) {
        digest.update(data);
        digest.digest();
      }

      // 性能测试
      long startTime = System.nanoTime();
      for (int i = 0; i < iterations; i++) {
        digest.update(data);
        digest.digest();
      }
      long endTime = System.nanoTime();

      // 计算并输出性能指标（总时间和平均时间）
      double milliseconds = (endTime - startTime) / 1_000_000.0;
      log.info("Provider: {}, Algorithm: {}", providerName, algorithm);
      log.info("Time for {} iterations: {} ms", iterations, milliseconds);
      log.info("Average time per operation: {} ms", milliseconds / iterations);

    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      log.error("Performance test failed", e);
    }
  }

  /**
   * 目的：用于测试随机数生成器的连续性，检测是否会产生相同的随机数序列
   * <p>
   * 功能：生成1000次32字节的随机数，并检查相邻的两次生成结果是否相同
   * <p>
   * 特点：使用 clone() 方法来保存前一次的结果，避免引用问题
   * <p>
   * 注意：此方法仅用于测试目的，实际应用中应避免使用相同的随机数序列
   *
   * @param secureRandom SecureRandom实例
   */
  public static void performContinuousTest(SecureRandom secureRandom) {
    byte[] previous = null;
    for (int i = 0; i < 1000; i++) {
      byte[] current = new byte[32];
      secureRandom.nextBytes(current);

      if (previous != null) {
        if (java.util.Arrays.equals(previous, current)) {
          System.out.println("Warning: Consecutive identical outputs detected!");
        }
      }
      previous = current.clone();
    }
  }

  /**
   * 打印 SecureRandom 实例的详细信息
   *
   * @param secureRandom SecureRandom 实例
   */
  public static void printSecureRandomDetails(SecureRandom secureRandom) {
    if (secureRandom == null) {
      System.out.println("SecureRandom instance is null");
      return;
    }

    // 打印基本信息
    System.out.println("\n=== SecureRandom Implementation Details ===");
    System.out.println("Algorithm: " + secureRandom.getAlgorithm());
    System.out.println("Implementation Class: " + secureRandom.getClass().getName());

    // 获取Provider信息
    Provider provider = secureRandom.getProvider();
    if (provider != null) {
      System.out.println("\n=== Provider Information ===");
      System.out.println("Provider Name: " + provider.getName());
      System.out.println("Provider Version: " + provider.getVersionStr());
      System.out.println("Provider Info: " + provider.getInfo());

      // 打印所有支持的算法
      System.out.println("\n=== Supported Services ===");
      provider.getServices().forEach(service -> {
        if ("SecureRandom".equals(service.getType())) {
          System.out.println("Service: " + service.getAlgorithm());
        }
      });

      // 打印所有可用的属性
      System.out.println("\n=== Provider Properties ===");
      provider.stringPropertyNames().forEach(prop -> {
        System.out.println(prop + ": " + provider.getProperty(prop));
      });
    }

    // 打印强随机数生成器标志
    System.out.println("\n=== Additional Information ===");
    System.out.println("Strong Instance: " + secureRandom.getClass().getName());
  }

  /**
   * 将字节数组转换为十六进制字符串
   *
   * @param bytes 字节数组
   * @return 十六进制字符串
   */
  public static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }
}

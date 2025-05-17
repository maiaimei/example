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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provider 管理工具类 提供 Provider 的添加、移除、状态管理等功能
 */
@Slf4j
public class ProviderUtils {

  private static final Object LOCK = new Object();
  private static final Set<String> ACTIVE_PROVIDERS = ConcurrentHashMap.newKeySet();

  /**
   * Provider 状态信息
   */
  public static class ProviderState {

    // Getters
    @Getter
    private final String name;
    private final boolean isInstalled;
    @Getter
    private final int position;
    @Getter
    private final String version;
    @Getter
    private final Set<String> supportedAlgorithms;

    public ProviderState(String name, boolean isInstalled, int position,
        String version, Set<String> supportedAlgorithms) {
      this.name = name;
      this.isInstalled = isInstalled;
      this.position = position;
      this.version = version;
      this.supportedAlgorithms = supportedAlgorithms;
    }

    public boolean isInstalled() {
      return isInstalled;
    }

    @Override
    public String toString() {
      return String.format("Provider[name=%s, installed=%b, position=%d, version=%s]",
          name, isInstalled, position, version);
    }
  }

  /**
   * 安全地添加 Provider
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
          log.info("Provider {} initialization success", providerName);
          log.info("Provider version: {}, Provider info: {}", provider.getVersionStr(), provider.getInfo());
        }
      }
    } catch (Exception e) {
      log.error("Provider initialization failed", e);
      throw new RuntimeException("Provider initialization failed", e);
    }
  }

  /**
   * 安全地移除 Provider
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

  public static boolean existProvider(Provider provider) {
    return Objects.nonNull(Security.getProvider(provider.getName()));
  }

  public static boolean existProvider(String providerName) {
    return Objects.nonNull(Security.getProvider(providerName));
  }

  /**
   * 检查 Provider 状态
   */
  public static ProviderState checkProviderState(String providerName) {
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
   * 获取 Provider 支持的算法
   */
  public static Set<String> getProviderAlgorithms(Provider provider) {
    Set<String> algorithms = new HashSet<>();
    for (Provider.Service service : provider.getServices()) {
      algorithms.add(service.getAlgorithm());
    }
    return algorithms;
  }

  /**
   * 临时使用 Provider
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
   * 临时使用 Provider
   */
  public static <T> T withProvider(Provider provider, Callable<T> action) {
    String providerName = provider.getName();
    addProvider(provider);
    try {
      if (!existProvider(provider)) {
        throw new RuntimeException("Provider " + providerName + " does not exist");
      }
      return action.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      removeProvider(providerName);
    }
  }

  /**
   * 临时使用 Provider
   */
  public static <T> T withProvider(Provider provider, Function<Provider, T> action) {
    String providerName = provider.getName();
    addProvider(provider);
    try {
      if (!existProvider(provider)) {
        throw new RuntimeException("Provider " + providerName + " does not exist");
      }
      return action.apply(provider);
    } catch (Exception e) {
      throw new RuntimeException(e);
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
   * 性能测试工具
   */
  public static void performanceTest(String providerName, String algorithm, int iterations) {
    try {
      Provider provider = Security.getProvider(providerName);
      if (provider == null) {
        throw new IllegalArgumentException("Provider not found: " + providerName);
      }

      // 预热
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

      double milliseconds = (endTime - startTime) / 1_000_000.0;
      log.info("Provider: {}, Algorithm: {}", providerName, algorithm);
      log.info("Time for {} iterations: {} ms", iterations, milliseconds);
      log.info("Average time per operation: {} ms", milliseconds / iterations);

    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      log.error("Performance test failed", e);
    }
  }
}

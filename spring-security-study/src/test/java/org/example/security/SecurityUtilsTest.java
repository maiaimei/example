package org.example.security;

import java.security.Provider;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.security.SecurityUtils.ProviderState;
import org.junit.jupiter.api.Test;

@Slf4j
public class SecurityUtilsTest {

  @Test
  public void testAddProvider() {
    // 安全地添加 Provider
    SecurityUtils.addProvider(new BouncyCastleProvider());
  }

  @Test
  public void testListInstalledProviders() {
    // 打印所有已安装的 Provider 信息
    SecurityUtils.listInstalledProviders();
  }

  @Test
  public void testProviderLifecycle() {
    // 安全地添加 Provider
    SecurityUtils.addProvider(new BouncyCastleProvider());

    // 打印所有已安装的 Provider 信息
    SecurityUtils.listInstalledProviders();

    // 获取 Provider 状态信息
    final ProviderState providerState = SecurityUtils.getProviderState("BC");
    System.out.println("Provider state: " + providerState);
    if (providerState.isInstalled()) {
      final Set<String> supportedAlgorithms = providerState.getSupportedAlgorithms();
      System.out.println("Supported Algorithms: " + supportedAlgorithms.size());
      for (String supportedAlgorithm : supportedAlgorithms) {
        System.out.println(supportedAlgorithm);
      }
    }

    // 安全地移除 Provider
    SecurityUtils.removeProvider("BC");
  }

  @Test
  public void testWithProvider_listInstalledProviders() {
    // 使用 Provider 执行操作，打印所有已安装的 Provider 信息
    SecurityUtils.withProvider(new BouncyCastleProvider(), SecurityUtils::listInstalledProviders);
  }

  @Test
  public void testWithProvider_getProviderAlgorithms() {
    // 使用 Provider 执行操作，获取 Provider 支持的算法集合
    SecurityUtils.withProvider(new BouncyCastleProvider(), () -> {
      final Provider provider = SecurityUtils.getProvider("BC");
      final Set<String> supportedAlgorithms = SecurityUtils.getProviderAlgorithms(provider);
      System.out.println("Supported Algorithms: " + supportedAlgorithms.size());
      for (String supportedAlgorithm : supportedAlgorithms) {
        System.out.println(supportedAlgorithm);
      }
    });
  }

  @Test
  public void testPerformanceTest() {
    // 使用 Provider 执行操作，性能测试
    SecurityUtils.withProvider(new BouncyCastleProvider(), () -> SecurityUtils.performanceTest(
        "BC",
        "SHA-256",
        1000));
  }

}

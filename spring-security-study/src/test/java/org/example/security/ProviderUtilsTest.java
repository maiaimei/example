package org.example.security;

import java.security.Provider;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.security.ProviderUtils.ProviderState;
import org.example.security.bc.BouncyCastleConstants;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProviderUtilsTest {

  @Test
  public void testAddProvider() {
    // 安全地添加 Provider
    ProviderUtils.addProvider(new BouncyCastleProvider());
  }

  @Test
  public void testListInstalledProviders() {
    // 打印所有已安装的 Provider 信息
    ProviderUtils.listInstalledProviders();
  }

  @Test
  public void testProviderLifecycle() {
    // 安全地添加 Provider
    ProviderUtils.addProvider(new BouncyCastleProvider());

    // 打印所有已安装的 Provider 信息
    ProviderUtils.listInstalledProviders();

    // 检查 Provider 状态
    final ProviderState providerState = ProviderUtils.getProviderState(BouncyCastleConstants.PROVIDER_NAME);
    System.out.println("Provider state: " + providerState);
    if (providerState.isInstalled()) {
      final Set<String> supportedAlgorithms = providerState.getSupportedAlgorithms();
      System.out.println("Supported Algorithms: " + supportedAlgorithms.size());
      for (String supportedAlgorithm : supportedAlgorithms) {
        System.out.println(supportedAlgorithm);
      }
    }

    // 安全地移除 Provider
    ProviderUtils.removeProvider(BouncyCastleConstants.PROVIDER_NAME);
  }

  @Test
  public void testWithProvider_listInstalledProviders() {
    ProviderUtils.withProvider(new BouncyCastleProvider(), ProviderUtils::listInstalledProviders);
  }

  @Test
  public void testWithProvider_getProviderAlgorithms() {
    ProviderUtils.withProvider(new BouncyCastleProvider(), () -> {
      final Provider provider = ProviderUtils.getProvider(BouncyCastleConstants.PROVIDER_NAME);
      final Set<String> supportedAlgorithms = ProviderUtils.getProviderAlgorithms(provider);
      System.out.println("Supported Algorithms: " + supportedAlgorithms.size());
      for (String supportedAlgorithm : supportedAlgorithms) {
        System.out.println(supportedAlgorithm);
      }
    });
  }

  @Test
  public void testPerformanceTest() {
    ProviderUtils.withProvider(new BouncyCastleProvider(), () -> ProviderUtils.performanceTest(
        BouncyCastleConstants.PROVIDER_NAME,
        BouncyCastleConstants.ALGORITHM_SHA_256,
        1000));
  }

}

package org.example;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.bc.BouncyCastleConstants;
import org.example.security.ProviderUtils;
import org.example.security.ProviderUtils.ProviderLifecycle;
import org.example.security.ProviderUtils.ProviderState;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProviderUtilsTest {

  @Test
  public void testBouncyCastleProviderLifecycleAndAlgorithmVerification() {
    // 安全地添加 Provider
    ProviderUtils.addProvider(new BouncyCastleProvider());

    // 打印所有已安装的 Provider 信息
    ProviderUtils.listInstalledProviders();

    // 检查 Provider 状态
    ProviderState bcState = ProviderUtils.checkProviderState(BouncyCastleConstants.PROVIDER_NAME);
    log.info("BC provider state: {}", bcState);

    // 安全地移除 Provider
    Set<String> required = new LinkedHashSet<>(Arrays.asList(
        BouncyCastleConstants.ALGORITHM_SHA_256,
        BouncyCastleConstants.ALGORITHM_AES,
        BouncyCastleConstants.ALGORITHM_RSA));
    Set<String> missing = ProviderUtils.checkMissingAlgorithms(BouncyCastleConstants.PROVIDER_NAME, required);
    log.info("Missing algorithms: {}", missing);

    // 安全地移除 Provider
    ProviderUtils.removeProvider("BC");
  }

  @Test
  public void testProviderLifecycle() {
    try {
      // 使用生命周期管理
      try (ProviderLifecycle lifecycle = new ProviderLifecycle(new BouncyCastleProvider())) {
        // 执行需要 BC 的操作
        lifecycle.ensureActive();

        // 性能测试
        ProviderUtils.performanceTest(BouncyCastleConstants.PROVIDER_NAME, BouncyCastleConstants.ALGORITHM_SHA_256,
            1000);
      }

    } catch (Exception e) {
      log.error("Error occurs", e);
    }
  }

}

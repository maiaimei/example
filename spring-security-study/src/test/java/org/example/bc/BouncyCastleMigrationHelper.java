package org.example.bc;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public class BouncyCastleMigrationHelper {

  @Data
  @AllArgsConstructor
  public static class MigrationResult {

    private final boolean needsMigration;
    private final String currentVersion;
    private final String recommendedVersion;
    private final List<String> incompatibleFeatures;
  }

  public static MigrationResult checkMigrationNeeded() {
    Provider provider = Security.getProvider(BouncyCastleConstants.PROVIDER_NAME);
    String currentVersion = provider != null ?
        String.valueOf(provider.getVersionStr()) : "Not installed";

    boolean needsMigration = provider != null && Integer.parseInt(provider.getVersionStr()) < 1.70;
    List<String> incompatibleFeatures = new ArrayList<>();

    // 检查特定功能
    try {
      SecureRandom.getInstance(BouncyCastleConstants.ALGORITHM_DRBG_CTR, BouncyCastleConstants.PROVIDER_NAME);
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      incompatibleFeatures.add(BouncyCastleConstants.ALGORITHM_DRBG_CTR);
    }

    return new MigrationResult(needsMigration, currentVersion,
        "bcprov-jdk18on", incompatibleFeatures);
  }
}

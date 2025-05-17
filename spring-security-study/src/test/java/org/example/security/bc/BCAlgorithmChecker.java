package org.example.security.bc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BCAlgorithmChecker {

  /**
   * 检查所有支持的算法
   */
  public static void listAllSupportedAlgorithms() {
    // 确保 BC provider 已注册
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    Provider provider = Security.getProvider("BC");

    // 创建类型映射
    Map<String, Set<String>> algorithmsByType = new TreeMap<>();

    // 遍历所有服务
    for (Provider.Service service : provider.getServices()) {
      String type = service.getType();
      String algorithm = service.getAlgorithm();

      algorithmsByType
          .computeIfAbsent(type, k -> new TreeSet<>())
          .add(algorithm);
    }

    // 打印结果
    System.out.println("BouncyCastle Provider Version: " + provider.getVersionStr());
    System.out.println("\nSupported Algorithms by Type:");

    for (Map.Entry<String, Set<String>> entry : algorithmsByType.entrySet()) {
      System.out.println("\n" + entry.getKey() + ":");
      for (String algorithm : entry.getValue()) {
        System.out.println("  - " + algorithm);
      }
    }
  }

  /**
   * 检查特定类型的算法
   */
  public static void listAlgorithmsOfType(String type) {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    Provider provider = Security.getProvider("BC");
    System.out.println("\nSupported " + type + " Algorithms:");

    for (Provider.Service service : provider.getServices()) {
      if (service.getType().equals(type)) {
        System.out.println("  - " + service.getAlgorithm());
      }
    }
  }

  /**
   * 检查 SecureRandom 实现
   */
  public static void listSecureRandomImplementations() {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    System.out.println("\nSecureRandom Implementations:");
    listAlgorithmsOfType("SecureRandom");
  }

  /**
   * 测试特定算法是否可用
   */
  public static boolean isAlgorithmAvailable(String type, String algorithm) {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    try {
      switch (type) {
        case "SecureRandom":
          SecureRandom.getInstance(algorithm, "BC");
          break;
        case "Cipher":
          Cipher.getInstance(algorithm, "BC");
          break;
        case "MessageDigest":
          MessageDigest.getInstance(algorithm, "BC");
          break;
        default:
          return false;
      }
      return true;
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
      return false;
    }
  }

  public static void main(String[] args) {
    // 1. 列出所有支持的算法
    System.out.println("Listing all supported algorithms...");
    listAllSupportedAlgorithms();

    // 2. 特别检查 SecureRandom 实现
    System.out.println("\nChecking SecureRandom implementations...");
    listSecureRandomImplementations();

    // 3. 测试特定算法
    String[] algorithmsToTest = {
        "DEFAULT",
        "SHA1PRNG",
        "DRBG",
        "NONCEANDIV"
    };

    System.out.println("\nTesting specific SecureRandom algorithms:");
    for (String algorithm : algorithmsToTest) {
      boolean isAvailable = isAlgorithmAvailable("SecureRandom", algorithm);
      System.out.printf("  - %-20s: %s%n", algorithm,
          isAvailable ? "Available" : "Not Available");
    }
  }
}

package org.example.security;

import java.security.Provider;
import java.security.SecureRandom;

public class SecurityUtils {

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

  public static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }
}

# Bouncy Castle

Bouncy Castle 是一个广泛使用的密码学库。

添加 Bouncy Castle 依赖

Maven:

```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk18on</artifactId>
    <version>1.77</version>
</dependency>
```

Gradle:

```groovy
implementation 'org.bouncycastle:bcprov-jdk18on:1.77'
```

bcprov-jdk15on: The Bouncy Castle Crypto package is a Java implementation of cryptographic algorithms. This jar contains JCE provider and lightweight API for the Bouncy Castle Cryptography APIs for JDK 1.5 and up.

[https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)

- 支持 JDK 1.5 及以上版本
- 已停止更新，1.70 是最后版本
- 不包含最新的安全修复
- 兼容性更好

bcprov-jdk18on: The Bouncy Castle Crypto package is a Java implementation of cryptographic algorithms. This jar contains the JCA/JCE provider and low-level API for the BC Java version 1.80 for Java 8 and later.

[https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on)

- 支持 JDK 1.8 及以上版本
- 持续更新中
- 包含最新的安全修复和功能
- 性能更好

## 通过 Bouncy Castle 实现 CTR_DRBG

如果操作系统不支持CTR_DRBG，可以通过引入第三方依赖来实现 CTR_DRBG。Bouncy Castle 提供了 DRBG 的实现。以下是详细的使用方法：

基本使用示例

```java
import org.bouncycastle.crypto.prng.drbg.CTRSPDRBGProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class CTRDRBGExample {
    static {
        // 注册 Bouncy Castle 提供者
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static SecureRandom getCTRDRBG() {
        try {
            // 获取 CTR_DRBG 实例
            return SecureRandom.getInstance("DRBG-CTR", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to initialize CTR_DRBG", e);
        }
    }
    
    public static void main(String[] args) {
        SecureRandom random = getCTRDRBG();
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
    }
}
```

封装工具类

```java
public class DRBGUtils {
    private static final SecureRandom CTR_DRBG;
    
    static {
        // 注册 Bouncy Castle 提供者
        Security.addProvider(new BouncyCastleProvider());
        
        try {
            CTR_DRBG = SecureRandom.getInstance("DRBG-CTR", "BC");
            // 预热
            CTR_DRBG.nextBytes(new byte[16]);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    // 生成随机字节数组
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        CTR_DRBG.nextBytes(bytes);
        return bytes;
    }
    
    // 生成随机整数
    public static int generateRandomInt(int bound) {
        return CTR_DRBG.nextInt(bound);
    }
    
    // 生成随机字符串
    public static String generateRandomString(int length) {
        byte[] bytes = generateRandomBytes(length);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
```

高级配置示例

```java
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;
import org.bouncycastle.crypto.prng.drbg.CTRDRBGProvider;

public class AdvancedCTRDRBG {
    private static final String BC_PROVIDER = "BC";
    
    public static class DRBGConfig {
        private final int strengthBits;
        private final byte[] personalizationString;
        private final boolean predictionResistance;
        
        public DRBGConfig(int strengthBits, byte[] personalizationString, 
                         boolean predictionResistance) {
            this.strengthBits = strengthBits;
            this.personalizationString = personalizationString;
            this.predictionResistance = predictionResistance;
        }
    }
    
    public static SecureRandom configureCTRDRBG(DRBGConfig config) {
        try {
            SecureRandom random = SecureRandom.getInstance("DRBG-CTR", BC_PROVIDER);
            
            // 设置个性化字符串
            if (config.personalizationString != null) {
                random.setSeed(config.personalizationString);
            }
            
            return random;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to configure CTR_DRBG", e);
        }
    }
}
```

线程安全实现

```java
public class ThreadSafeDRBG {
    private static final ThreadLocal<SecureRandom> THREAD_LOCAL_DRBG = 
        ThreadLocal.withInitial(() -> {
            try {
                return SecureRandom.getInstance("DRBG-CTR", "BC");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException("Failed to initialize CTR_DRBG", e);
            }
        });
    
    public static byte[] generateRandomBytes(int length) {
        SecureRandom random = THREAD_LOCAL_DRBG.get();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
}
```

性能测试工具

```java
public class DRBGPerformanceTest {
    public static void measurePerformance() {
        SecureRandom random = DRBGUtils.getCTRDRBG();
        byte[] bytes = new byte[1024];
        
        // 预热
        for (int i = 0; i < 100; i++) {
            random.nextBytes(bytes);
        }
        
        // 性能测试
        int iterations = 1000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            random.nextBytes(bytes);
        }
        
        long endTime = System.nanoTime();
        double milliseconds = (endTime - startTime) / 1_000_000.0;
        
        System.out.printf("Generated %d KB in %.2f ms%n", 
            iterations, milliseconds);
        System.out.printf("Average time per KB: %.3f ms%n", 
            milliseconds / iterations);
    }
}
```

实际应用示例

```java
// 密钥生成：
public class KeyGenerator {
    private static final SecureRandom DRBG = DRBGUtils.getCTRDRBG();
    
    public static byte[] generateAESKey() {
        byte[] key = new byte[32]; // 256-bit key
        DRBG.nextBytes(key);
        return key;
    }
    
    public static byte[] generateIV() {
        byte[] iv = new byte[16];
        DRBG.nextBytes(iv);
        return iv;
    }
}
```

```java
// 会话ID生成：
public class SessionManager {
    private static final SecureRandom DRBG = DRBGUtils.getCTRDRBG();
    
    public static String generateSessionId() {
        byte[] sessionId = new byte[16];
        DRBG.nextBytes(sessionId);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sessionId);
    }
}
```

错误处理和降级策略

```java
public class DRBGFallback {
    public static SecureRandom getSecureRandom() {
        try {
            // 尝试获取 CTR_DRBG
            return SecureRandom.getInstance("DRBG-CTR", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
            try {
                // 尝试获取默认 DRBG
                return SecureRandom.getInstance("DRBG");
            } catch (NoSuchAlgorithmException e2) {
                // 降级到默认 SecureRandom
                return new SecureRandom();
            }
        }
    }
}
```

package org.example.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BCryptPasswordEncoderWrapperTest {

  private BCryptPasswordEncoderWrapper encoder;

  @BeforeEach
  void setUp() {
    encoder = new BCryptPasswordEncoderWrapper();
  }

  @Test
  @DisplayName("基本加密和验证测试")
  void testBasicEncodeAndMatch() {
    String rawPassword = "TestPassword123!";
    String encodedPassword = encoder.encode(rawPassword);
    System.out.println("encodedPassword: " + encodedPassword);

    assertNotNull(encodedPassword);
    assertTrue(encoder.matches(rawPassword, encodedPassword));
    assertFalse(encoder.matches("WrongPassword", encodedPassword));
  }

  @Test
  @DisplayName("测试验证已存在的密码")
  void testMatchExistingEncodedPassword() {
    String rawPassword = "TestPassword123!";
    String encodedPassword = "JDJiJDE0JGZoeFFaUWV4RlN1QXd6dUZFM29xNWVzakNUb2ZJVWxvNjh0NjVaQWpiNWpDUWkwcEpVeVNt";

    assertTrue(encoder.matches(rawPassword, encodedPassword));
  }

  @Test
  @DisplayName("测试空密码处理")
  void testEmptyPassword() {
    String emptyPassword = "";
    assertThrows(IllegalArgumentException.class, () -> encoder.encode(emptyPassword));
  }

  @Test
  @DisplayName("测试null密码处理")
  void testNullPassword() {
    assertThrows(IllegalArgumentException.class, () -> encoder.encode(null));
    assertThrows(IllegalArgumentException.class, () -> encoder.matches(null, "encoded"));
  }

  @ParameterizedTest
  @DisplayName("测试特殊字符密码")
  @ValueSource(strings = {
      "!@#$%^&*()_+-=[]{}|;:,.<>?",
      "密码测试🔒",
      "Password with spaces",
      "12345678901234567890"
  })
  void testSpecialCharacters(String password) {
    String encoded = encoder.encode(password);
    assertTrue(encoder.matches(password, encoded));
  }

  @Test
  @DisplayName("测试Base64编码格式")
  void testBase64EncodingFormat() {
    String password = "TestPassword";
    String encoded = encoder.encode(password);

    // 验证Base64格式
    assertDoesNotThrow(() -> Base64.getDecoder().decode(encoded));
  }

  @Test
  @DisplayName("测试密码加密唯一性")
  void testEncodingUniqueness() {
    String password = "SamePassword";
    Set<String> encodedPasswords = new HashSet<>();

    // 同一密码加密多次应产生不同的结果
    for (int i = 0; i < 10; i++) {
      encodedPasswords.add(encoder.encode(password));
    }

    assertEquals(10, encodedPasswords.size());
  }

  @Test
  @DisplayName("测试长密码")
  void testLongPassword() {
    // BCrypt限制为72字节
    String longPassword = "a".repeat(100);
    assertThrows(IllegalArgumentException.class, () -> encoder.encode(longPassword));
  }

  @Test
  @DisplayName("测试无效的编码密码")
  void testInvalidEncodedPassword() {
    String password = "test";

    // 测试非Base64编码
    assertThrows(IllegalArgumentException.class, () -> encoder.matches(password, "invalid base64"));

    // 测试空编码密码
    assertThrows(IllegalArgumentException.class, () -> encoder.matches(password, ""));

    // 测试null编码密码
    assertThrows(IllegalArgumentException.class, () -> encoder.matches(password, null));
  }

  @Test
  @DisplayName("测试并发加密")
  void testConcurrentEncoding() throws InterruptedException {
    int threadCount = 10;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);
    Set<String> results = new HashSet<>();
    String password = "ConcurrentTest";

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          results.add(encoder.encode(password));
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executor.shutdown();

    assertEquals(threadCount, results.size());
  }

  @Test
  @DisplayName("测试密码验证的一致性")
  void testMatchConsistency() {
    String password = "TestPassword";
    String encoded = encoder.encode(password);

    // 多次验证应该返回相同结果
    for (int i = 0; i < 10; i++) {
      assertTrue(encoder.matches(password, encoded));
    }
  }

  @Test
  @DisplayName("测试不同长度的密码")
  void testVariousPasswordLengths() {
    String[] passwords = {
        "a",
        "ab",
        "abc",
        "a".repeat(50),
        "a".repeat(71),
        "a".repeat(72)
    };

    for (String password : passwords) {
      String encoded = encoder.encode(password);
      assertTrue(encoder.matches(password, encoded));
    }
  }

  @Test
  @DisplayName("测试编码结果的字节长度")
  void testEncodedLength() {
    String password = "TestPassword";
    String encoded = encoder.encode(password);

    // 解码Base64后应该是有效的BCrypt哈希
    byte[] decodedBytes = Base64.getDecoder().decode(encoded);
    String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

    // BCrypt哈希应该是60字符
    assertEquals(60, decodedString.length());
  }

  @Test
  @DisplayName("测试性能")
  void testPerformance() {
    String password = "PerformanceTest";
    long startTime = System.nanoTime();

    // 执行加密
    String encoded = encoder.encode(password);

    long encodeTime = System.nanoTime() - startTime;
    startTime = System.nanoTime();

    // 执行验证
    assertTrue(encoder.matches(password, encoded));

    long matchTime = System.nanoTime() - startTime;

    // 输出性能指标（毫秒）
    System.out.printf("Encode time: %.2f ms%n", encodeTime / 1_000_000.0);
    System.out.printf("Match time: %.2f ms%n", matchTime / 1_000_000.0);
  }

  @Test
  @DisplayName("测试边界情况")
  void testEdgeCases() {
    // 测试空白字符
    String[] whitespacePasswords = {
        " ",
        "\t",
        "\n",
        "\r",
        "\f",
        "  \t  \n  \r  \f  "
    };

    for (String password : whitespacePasswords) {
      String encoded = encoder.encode(password);
      assertTrue(encoder.matches(password, encoded));
    }
  }

  @Test
  @DisplayName("测试错误的Base64编码处理")
  void testInvalidBase64Handling() {
    String password = "test";
    String[] invalidEncodings = {
        "!invalid base64!",
        "===",
        "SGVsbG8gV29ybGQh=" // 有效Base64但不是BCrypt哈希
    };

    for (String invalidEncoding : invalidEncodings) {
      assertThrows(IllegalArgumentException.class, () -> encoder.matches(password, invalidEncoding));
    }
  }
}

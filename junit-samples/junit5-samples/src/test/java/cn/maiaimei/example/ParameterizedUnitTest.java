package cn.maiaimei.example;

import java.lang.annotation.ElementType;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class ParameterizedUnitTest {

  @ValueSource(strings = {"admin", "guest"})
  @ParameterizedTest
  public void testValueSource(String username) {
    log.info("username: {}", username);
  }

  @NullSource
  @ParameterizedTest
  public void testNullSource(String username) {
    Assertions.assertNull(username);
  }

  @EmptySource
  @ParameterizedTest
  public void testEmptySource(String username) {
    Assertions.assertEquals("", username);
  }

  @NullAndEmptySource
  @ParameterizedTest
  public void testNullAndEmptySource(String username) {
    Assertions.assertEquals("", Optional.ofNullable(username).orElse(""));
  }

  @EnumSource
  @ParameterizedTest
  public void testEnumSource1(ElementType elementType) {
    Assertions.assertNotNull(elementType);
  }

  @EnumSource(value = ElementType.class, names = {"TYPE", "METHOD", "FIELD"})
  @ParameterizedTest
  public void testEnumSource2(ElementType elementType) {
    Assertions.assertNotNull(elementType);
  }

  /**
   * 工厂方法与测试方法在同一个类中
   */
  @MethodSource("methodSource1")
  @ParameterizedTest
  public void testMethodSource1(String username) {
    log.info("username: {}", username);
  }

  /**
   * @MethodSource 提供单组参数。
   */
  static Stream<String> methodSource1() {
    return Stream.of("admin");
  }

  /**
   * 工厂方法与测试方法在同一个类中
   */
  @MethodSource("methodSource2")
  @ParameterizedTest
  public void testMethodSource2(String username) {
    log.info("username: {}", username);
  }

  /**
   * @MethodSource 提供单组参数。
   */
  static Stream<String> methodSource2() {
    return Stream.of("admin", "guest");
  }

  /**
   * 工厂方法与测试方法在同一个类中
   */
  @MethodSource("methodSource3")
  @ParameterizedTest
  public void testMethodSource3(String username, String password) {
    log.info("username: {}, password: {}", username, password);
  }

  /**
   * @MethodSource 提供多组参数
   */
  static Stream<Arguments> methodSource3() {
    return Stream.of(
        Arguments.of("admin", "a1234"),
        Arguments.of("guest", "b1234")
    );
  }

  /**
   * 工厂方法与测试方法不在同一个类中
   * <p>注意 value 值的书写方式，即类名与工厂方法名之间以井号（#）进行了分隔。</p>
   */
  @MethodSource("cn.maiaimei.example.MethodSourceClass#methodSource4")
  @ParameterizedTest
  public void testMethodSource4(String username, String password) {
    log.info("username: {}, password: {}", username, password);
  }

  @CsvSource(value = {"admin,a1234", "guest,b1234"})
  @ParameterizedTest
  public void testCsvSource(String username, String password) {
    log.info("username: {}, password: {}", username, password);
  }

  @CsvFileSource(resources = "/test.csv")
  @ParameterizedTest
  public void testCsvFileSource(String username, String password) {
    log.info("username: {}, password: {}", username, password);
  }

}

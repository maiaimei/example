package cn.maiaimei.example;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class MethodSourceClass {

  /**
   * @MethodSource 提供多组参数
   */
  public static Stream<Arguments> methodSource4() {
    return Stream.of(
        Arguments.of("admin", "a1234"),
        Arguments.of("guest", "b1234")
    );
  }
}

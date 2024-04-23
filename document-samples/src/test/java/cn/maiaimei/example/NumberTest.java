package cn.maiaimei.example;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.NumberUtils;

/**
 * {@link Digits}
 * <p>
 * {@link ConstraintValidator}
 * <p>
 * {@link DigitsValidatorForNumber}
 * <p>
 * {@link Converter}
 */
@Slf4j
public class NumberTest {

  @Test
  public void testIsNumeric() {
    Assertions.assertTrue(StringUtils.isNumeric("123456"));
    Assertions.assertFalse(StringUtils.isNumeric("123.456"));
    Assertions.assertTrue(StringUtils.isNumericSpace("123456 "));
    Assertions.assertFalse(StringUtils.isNumericSpace("123.456 "));
  }

  @Test
  public void testIsDecimal() {
    String[] testStrings = {null, "", "123", "123.45", "0.123", ".123", "abc", "123.45.67"};
    for (String str : testStrings) {
      log.info("{} is decimal? {}", str, isDecimal(str));
    }
  }

  @Test
  public void testIsDigits() {
    String[] testStrings = {null, "", "123", "123.45", "0.123", ".123", "abc", "123.45.67"};
    for (String str : testStrings) {
      log.info("{} is digits? {}", str, isDigits(str));
    }
  }

  @Test
  public void testValidate() {
    String text = "123.456";
    final Number num = NumberUtils.parseNumber(text, Number.class);
    int precision = 18;
    int scale = 3;
    final boolean result = validate(num, precision - scale, scale);
    Assertions.assertTrue(result);
  }

  private boolean isDecimal(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    String regex = "^\\d+\\.\\d+$";
    return str.matches(regex);
  }

  private boolean isDigits(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    String regex = "^\\d+(\\.\\d+)?$";
    return str.matches(regex);
  }

  private boolean validate(Number num, int maxIntegerLength, int maxFractionLength) {
    if (num == null) {
      return true;
    } else {
      BigDecimal bigNum;
      if (num instanceof BigDecimal) {
        bigNum = (BigDecimal) num;
      } else {
        bigNum = (new BigDecimal(num.toString())).stripTrailingZeros();
      }

      int integerPartLength = bigNum.precision() - bigNum.scale();
      int fractionPartLength = Math.max(bigNum.scale(), 0);
      return maxIntegerLength >= integerPartLength && maxFractionLength >= fractionPartLength;
    }
  }

}

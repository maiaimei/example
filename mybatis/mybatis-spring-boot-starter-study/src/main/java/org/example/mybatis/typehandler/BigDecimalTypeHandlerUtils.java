package org.example.mybatis.typehandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BigDecimalTypeHandlerUtils {

  /**
   * 将逗号分隔的字符串解析为 List<BigDecimal>
   *
   * @param value 逗号分隔的字符串
   * @return List<BigDecimal>
   */
  public static List<BigDecimal> parseBigDecimalList(String value) {
    if (value == null || value.isEmpty()) {
      return new ArrayList<>();
    }
    String[] parts = value.split(",");
    List<BigDecimal> result = new ArrayList<>();
    for (String part : parts) {
      result.add(new BigDecimal(part.trim()));
    }
    return result;
  }

  /**
   * 将逗号分隔的字符串解析为 BigDecimal[]
   *
   * @param value 逗号分隔的字符串
   * @return BigDecimal[]
   */
  public static BigDecimal[] parseBigDecimalArray(String value) {
    if (value == null || value.isEmpty()) {
      return new BigDecimal[0];
    }
    String[] parts = value.split(",");
    return Arrays.stream(parts)
        .map(String::trim)
        .map(BigDecimal::new)
        .toArray(BigDecimal[]::new);
  }

  /**
   * 将 List<BigDecimal> 转换为逗号分隔的字符串
   *
   * @param list List<BigDecimal>
   * @return 逗号分隔的字符串
   */
  public static String convertListToString(List<BigDecimal> list) {
    StringBuilder sb = new StringBuilder();
    for (BigDecimal bigDecimal : list) {
      sb.append(bigDecimal.toString()).append(",");
    }
    return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
  }

  /**
   * 将 BigDecimal[] 转换为逗号分隔的字符串
   *
   * @param array BigDecimal[]
   * @return 逗号分隔的字符串
   */
  public static String convertArrayToString(BigDecimal[] array) {
    StringBuilder sb = new StringBuilder();
    for (BigDecimal bigDecimal : array) {
      sb.append(bigDecimal.toString()).append(",");
    }
    return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
  }
}
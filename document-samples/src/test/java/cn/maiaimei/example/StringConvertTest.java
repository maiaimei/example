package cn.maiaimei.example;

import static cn.maiaimei.commons.lang.constants.StringConstants.ARROW;
import static cn.maiaimei.commons.lang.constants.StringConstants.COMMA;
import static cn.maiaimei.commons.lang.constants.StringConstants.SEMICOLON;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link Converter}
 * <p>
 * {@link GenericConverter}
 */
@Slf4j
public class StringConvertTest {

  @Test
  public void testStringToArray() {
    String source = "field1,field2,field3,field4,field5";
    String[] fields = StringUtils.commaDelimitedListToStringArray(source);
    for (String field : fields) {
      log.info("{}", field);
    }
  }

  @Test
  public void testStringToList() {
    String source = "field1->yyyyMMdd,field2->yyyy-MM-dd";
    final String[] fieldConfigs = StringUtils.split(source, COMMA);
    Assert.notNull(fieldConfigs, "fieldConfigs must not be null");
    final List<String[]> fieldConfigList = Arrays.stream(fieldConfigs)
        .map(fieldConfig -> StringUtils.split(fieldConfig, ARROW))
        .collect(Collectors.toList());
    for (String[] item : fieldConfigList) {
      log.info("name: {}, format: {}", item[0], item[1]);
    }
  }

  @Test
  public void testStringToMap1() {
    String source = "field1->yyyyMMdd,field2->yyyy-MM-dd";
    final String[] fieldConfigs = StringUtils.split(source, COMMA);
    Assert.notNull(fieldConfigs, "fieldConfigs must not be null");
    final Map<String, String> map = Arrays.stream(fieldConfigs)
        .map(fieldConfig -> StringUtils.split(fieldConfig, ARROW))
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(
            entry -> entry[0], // 使用数组的第一个元素作为键
            entry -> entry[1]  // 使用数组的第二个元素作为值
        ));
    for (Entry<String, String> entry : map.entrySet()) {
      log.info("name: {}, format: {}", entry.getKey(), entry.getValue());
    }
  }

  @Test
  public void testStringToMap2() {
    String source = "field1->18,3;field2->9,6";
    final String[] fieldConfigs = StringUtils.split(source, SEMICOLON);
    Assert.notNull(fieldConfigs, "fieldConfigs must not be null");
    final Map<String, String[]> map = Arrays.stream(fieldConfigs)
        .map(fieldConfig -> StringUtils.split(fieldConfig, ARROW))
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(
            entry -> entry[0], // 使用数组的第一个元素作为键
            entry -> Optional.ofNullable(StringUtils.split(entry[1], COMMA))
                .orElseGet(() -> new String[0])  // 使用数组的第二个元素作为值
        ));
    map.forEach((field, lengths) -> {
      int precision = Integer.parseInt(lengths[0]);
      int scale = Integer.parseInt(lengths[1]);
      log.info("name: {}, precision: {}, scale: {}", field, precision, scale);
    });
  }

}

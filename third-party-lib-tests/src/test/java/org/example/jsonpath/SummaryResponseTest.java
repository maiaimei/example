package org.example.jsonpath;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class SummaryResponseTest extends BaseTest {

  private String content;

  @BeforeEach
  public void setup() {
    content = readFileContent("jsonpath/summary-response.json");
  }

  /**
   * 提取直接孩子节点。语法：$.name 或 $.['name']
   */
  @Test
  public void extractDirectChildNode() {
    final Object code1 = JsonPath.read(content, "$.code");
    final Object code2 = JsonPath.read(content, "$['code']");
    log.info("$.code = {}", code1);
    log.info("$['code'] = {}", code2);
    Assertions.assertEquals(code1, code2);
  }

  /**
   * 提取后代节点。语法：$..name
   */
  @Test
  public void extractDescendantsNode() {
    final Object names = JsonPath.read(content, "$..name");
    log.info("{}", names);
  }

  /**
   * 数组索引。语法：$.array[number1 (, number1)]
   * <p>
   * 数组切片。语法：$.array[start:end]
   */
  @Test
  public void testArrayIndexAndSlice() {
    // $.data[0] 表示提取数组第1个元素
    final Object firstElement = JsonPath.read(content, "$.data[0]");
    log.info("{}", firstElement);

    // $.data[0,1] 表示提取数组第1个元素和第2个元素
    final Object theFirstTwoElements = JsonPath.read(content, "$.data[0,1]");
    log.info("{}", theFirstTwoElements);

    // $.data[1:2] 表示提取数组第1到第2个元素
    final Object theOneToTwoElements = JsonPath.read(content, "$.data[0:2]");
    log.info("{}", theOneToTwoElements);
  }

  /**
   * 提取数组元素属性。语法：$.array[number]['name1' (, 'name2')]
   */
  @Test
  public void extractElementFromArray() {
    // $.data[1]['id'] 表示提取数组第2个元素的id
    final Object id = JsonPath.read(content, "$.data[1]['id']");
    log.info("{}", id);

    // $.data[1]['name'] 表示提取数组第2个元素的name
    final Object name = JsonPath.read(content, "$.data[1]['name']");
    log.info("{}", name);

    // $.data[1]['id','name'] 表示提取数组第2个元素的id和name
    final Object idAndName = JsonPath.read(content, "$.data[1]['id','name']");
    log.info("{}", idAndName);
  }

  /**
   * 条件过滤。语法：$.array[?(expression)]
   */
  @Test
  public void testFilterExpression() {
    // equal to
    final Object o1 = JsonPath.read(content, "$.data[?(@.sex == 'female')]");
    log.info("{}", o1);

    // greater than
    final Object o2 = JsonPath.read(content, "$.data[?(@.birthday >= '19900101')]");
    log.info("{}", o2);
  }

}

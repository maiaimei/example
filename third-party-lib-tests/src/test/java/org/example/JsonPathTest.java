package org.example;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JsonPathTest extends BaseTest {

  @Test
  public void testSummaryResponse() {
    final String content = readFileContent("json-path/summary-response.json");

    // $.code 表示直接孩子节点code, $.code 与 $['code'] 等价
    final Object code1 = JsonPath.read(content, "$.code");
    final Object code2 = JsonPath.read(content, "$['code']");
    log.info("{}", code1);
    log.info("{}", code2);

    // $..name 表示提取任意后代的name
    final Object names = JsonPath.read(content, "$..name");
    log.info("{}", names);

    // $.data[0] 表示数组的第1个元素
    final Object firstElement = JsonPath.read(content, "$.data[0]");
    log.info("{}", firstElement);
    // $.data[0,1] 表示数组的前2个元素
    final Object theFirstTwoElements = JsonPath.read(content, "$.data[0,1]");
    log.info("{}", theFirstTwoElements);
    // $.data[1:2] 表示数组的第1到2个元素
    final Object theOneToTwoElements = JsonPath.read(content, "$.data[0:2]");
    log.info("{}", theOneToTwoElements);

    // $.data[0]['id','name'] 表示提取数组第1个元素的id和name
    final Object multipleAttributes = JsonPath.read(content, "$.data[0]['id','name']");
    log.info("{}", multipleAttributes);

    // 条件过滤
    final Object filterElements1 = JsonPath.read(content, "$.data[?(@.sex=='female')]");
    log.info("{}", filterElements1);
  }

}

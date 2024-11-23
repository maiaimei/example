package org.example.jsonpath;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class ClientCodeTest extends BaseTest {

  private String json;

  @BeforeEach
  public void setup() {
    json = readFileContent("jsonpath/client-code.json");
  }

  @Test
  public void test1() {
    log.info("{}", JsonPath.parse(json).read("$[0].content", Map.class));
    log.info("{}", JsonPath.parse(json).read("$[0].content.DEV", String.class));
    log.info("{}", JsonPath.parse(json).read("$[0]['content']['DEV']", String.class));
  }

  @Test
  public void test2() {
    // com.jayway.jsonpath.PathNotFoundException: Expected to find an object with property ['DEV'] in path $[0]['extension'] but 
    // found 'java.lang.String'. This is not a json object according to the JsonProvider: 'com.jayway.jsonpath.spi.json
    // .JsonSmartJsonProvider'.
    assertThrows(PathNotFoundException.class, () -> JsonPath.read(json, "$[0].extension.DEV"));
  }

  @Test
  public void test3() {
    // com.jayway.jsonpath.PathNotFoundException: Expected to find an object with property ['DEV'] in path $[0]['extension'] but 
    // found 'java.lang.String'. This is not a json object according to the JsonProvider: 'com.jayway.jsonpath.spi.json
    // .JsonSmartJsonProvider'.
    assertThrows(PathNotFoundException.class, () -> JsonPath.read(json, "$[0].extension['DEV']"));
  }

}

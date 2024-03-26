package cn.maiaimei.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
public class SpELMapTest {

  @Test
  public void test_get() {
    String activeProfile = "DEV";
    Map<String, Object> rootObject = new HashMap<>();
    rootObject.put("activeProfile", activeProfile);
    // 创建评估上下文
    StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
    // 创建表达式解析器
    ExpressionParser parser = new SpelExpressionParser();
    // 使用SpEL获取Map的属性
    assertEquals(activeProfile,
        parser.parseExpression("#root['activeProfile']").getValue(context, String.class));
    assertEquals(activeProfile,
        parser.parseExpression("['activeProfile']").getValue(context, String.class));
    assertEquals(activeProfile,
        parser.parseExpression("[\"activeProfile\"]").getValue(context, String.class));
  }

}

package org.example.spel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELTest {

  @Test
  public void testString() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals("Any string", parser.parseExpression("'Any string'").getValue(String.class));
    Assertions.assertEquals(10, parser.parseExpression("'Any string'.length()").getValue(Integer.class));
    Assertions.assertEquals(10, parser.parseExpression("new String('Any string').length()").getValue(Integer.class));
    Assertions.assertEquals(9, parser.parseExpression("'Any string'.replace(\" \", \"\").length()").getValue(Integer.class));
    Assertions.assertNotNull(parser.parseExpression("'Any string'.bytes").getValue(byte[].class));
    Assertions.assertEquals("String1 string2", parser.parseExpression("'String1 ' + 'string2'").getValue(String.class));
    Assertions.assertEquals("String1 string2", parser.parseExpression("'String1 '.concat('string2')").getValue(String.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("'Hello, SpEL'.contains('SpEL')").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("'Hello, SpEL'.equals('SpEL')").getValue(Boolean.class));
  }

  @Test
  public void testArithmeticOperators() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals(20, parser.parseExpression("19 + 1").getValue(Integer.class));
    Assertions.assertEquals(19, parser.parseExpression("20 - 1").getValue(Integer.class));
    Assertions.assertEquals(20, parser.parseExpression("10 * 2").getValue(Integer.class));
    Assertions.assertEquals(18, parser.parseExpression("36 / 2").getValue(Integer.class));
    Assertions.assertEquals(7, parser.parseExpression("37 % 10").getValue(Integer.class));
    Assertions.assertEquals(512, parser.parseExpression("2 ^ 9").getValue(Integer.class));
    Assertions.assertEquals(18, parser.parseExpression("36 div 2").getValue(Integer.class)); // the same as for / operator
    Assertions.assertEquals(7, parser.parseExpression("37 mod 10").getValue(Integer.class)); // the same as for % operator
  }

  @Test
  public void testRelationalOperators() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 < 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 > 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 == 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 != 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 <= 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 >= 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 lt 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 gt 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 eq 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("1 ne 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 le 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 ge 1").getValue(Boolean.class));
  }

  @Test
  public void testLogicalOperators() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 < 2 && 2 > 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 < 2 || 2 > 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("!(1 != 1)").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 < 2 and 2 > 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("1 < 2 or 2 > 1").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("not(1 != 1)").getValue(Boolean.class));
  }

  @Test
  public void testConditionalOperators() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals("a", parser.parseExpression("2 > 1 ? 'a' : 'b'").getValue(String.class));
  }

  @Test
  public void testRegex() {
    ExpressionParser parser = new SpelExpressionParser();
    Assertions.assertEquals(Boolean.TRUE, parser.parseExpression("'100' matches '\\d+'").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, parser.parseExpression("'100fghdjf' matches '\\d+'").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.TRUE,
        parser.parseExpression("'valid alphabetic string' matches '[a-zA-Z\\s]+'").getValue(Boolean.class));
    Assertions.assertEquals(Boolean.FALSE,
        parser.parseExpression("'invalid alphabetic string #$1' matches '[a-zA-Z\\s]+'").getValue(Boolean.class));
  }

}

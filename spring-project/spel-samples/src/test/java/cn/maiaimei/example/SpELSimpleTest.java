package cn.maiaimei.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.maiaimei.example.model.Inventor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
public class SpELSimpleTest {

  @Test
  public void test_literal_string() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'");
    String message = (String) exp.getValue();
    log.info("{}", message);
    assertEquals("Hello World", message);
  }

  @Test
  public void test_types() {
    // https://docs.spring.io/spring-framework/reference/core/expressions/language-ref/types.html
    ExpressionParser parser = new SpelExpressionParser();
    // 使用“T(Type)”来表示java.lang.Class类的实例，即如同java代码中直接写类名。
    // 同样，只有java.lang 下的类才可以省略包名。此方法一般用来引用常量或静态方法
    Expression exp = parser.parseExpression("T(Integer).MAX_VALUE");// 等同于java代码中的：Integer.MAX_VALUE
    final Integer maxValue = exp.getValue(Integer.class);
    log.info("{}", maxValue);
    assertTrue(maxValue > 0);
  }

  @Test
  public void test_calling_constructor() {
    ExpressionParser parser = new SpelExpressionParser();
    // 如在SpEL中直接试用new/instance of，像写Java代码一样。
    // 注意：在SpEL中直接使用某个类名时，此类必须是java.lang包中的类，才可以在SpEL中省略包名；否则需要写全名
    Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
    String message = exp.getValue(String.class);
    log.info("{}", message);
    assertEquals("HELLO WORLD", message);
  }

  @Test
  public void test_calling_method() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'.concat('!')");
    String message = (String) exp.getValue();
    log.info("{}", message);
    assertEquals("Hello World!", message);
  }

  @Test
  public void test_accessing_property_01() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'.bytes");
    byte[] bytes = (byte[]) exp.getValue();
    log.info("{}", bytes);
    assertNotNull(bytes);
  }

  /**
   * SpEL also supports nested properties by using the standard dot notation (such as
   * prop1.prop2.prop3) and also the corresponding setting of property values. Public fields may
   * also be accessed.
   */
  @Test
  public void test_accessing_property_02() {
    ExpressionParser parser = new SpelExpressionParser();
    // invokes 'getBytes().length'
    Expression exp = parser.parseExpression("'Hello World'.bytes.length");
    int length = (Integer) exp.getValue();
    log.info("{}", length);
    assertTrue(length > 0);
  }

  @Test
  public void test_root_object() {
    // Create and set a calendar
    GregorianCalendar c = new GregorianCalendar();
    c.set(1856, 7, 9);

    // The constructor arguments are name, birthday, and nationality.
    Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

    ExpressionParser parser = new SpelExpressionParser();

    Expression exp = parser.parseExpression("name"); // Parse name as an expression
    String name = (String) exp.getValue(tesla);
    // name == "Nikola Tesla"
    log.info("{}", name);

    exp = parser.parseExpression("name == 'Nikola Tesla'");
    boolean result = exp.getValue(tesla, Boolean.class);
    // result == true
    assertTrue(result);
  }

  @Test
  public void test_operators() {
    // https://docs.spring.io/spring-framework/reference/core/expressions/language-ref/operators.html#expressions-operators-logical
    ExpressionParser parser = new SpelExpressionParser();

    // Relational Operators, all of the textual operators are case-insensitive.
    // lt (<) gt (>) le (<=) ge (>=) eq (==) ne (!=) div (/) mod (%) not (!)
    log.info("{}", parser.parseExpression("2 == 2").getValue(Boolean.class)); // true
    log.info("{}", parser.parseExpression("2 eq 2").getValue(Boolean.class)); // true
    log.info("{}", parser.parseExpression("2 EQ 2").getValue(Boolean.class)); // true
    log.info("{}", parser.parseExpression("1>2").getValue(Boolean.class)); // false
    log.info("{}", parser.parseExpression("'black' < 'block'").getValue(Boolean.class)); // true
    log.info("{}", parser.parseExpression("'black' == 'black'").getValue(Boolean.class)); // true
    // uses CustomValue:::compareTo
    // parser.parseExpression("new CustomValue(1) < new CustomValue(2)").getValue(Boolean.class);
    log.info("{}", parser.parseExpression(
        "'xyz' instanceof T(Integer)").getValue(Boolean.class)); // false
    log.info("{}", parser.parseExpression(
        "'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class)); // true
    log.info("{}", parser.parseExpression(
        "'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class)); // false

    // Logical Operators
    // and (&&) or (||) not (!)
    log.info("{}", parser.parseExpression("true and false").getValue(Boolean.class)); // false
    log.info("{}", parser.parseExpression("not true").getValue(Boolean.class)); // false
    log.info("{}", parser.parseExpression("!true").getValue(Boolean.class)); // false

    // Mathematical Operators
    // addition(+) subtraction(-) multiplication(*) division(/) modulus(%) power(^)
    log.info("{}", parser.parseExpression("1+2-3*4/2").getValue(Integer.class)); // -3

    // The Assignment Operator
    EvaluationContext context = new StandardEvaluationContext();
    context.setVariable("name", "Amy");
    log.info("{}", parser.parseExpression("#name").getValue(context, String.class));
    parser.parseExpression("#name").setValue(context, "May");
    log.info("{}", parser.parseExpression("#name").getValue(context, String.class));
    log.info("{}", parser.parseExpression("#name='Amy'").getValue(context, String.class));

    // The Elvis Operator
    log.info("{}", parser.parseExpression("#name?:'Unknown'").getValue(context, String.class));
    log.info("{}", parser.parseExpression("#sex?:'Unknown'").getValue(context, String.class));
  }

  @Test
  public void test_list() {
    ExpressionParser parser = new SpelExpressionParser();
    // 使用“{表达式，……}”定义List，如“{1,2,3}”
    // 对于字面量表达式列表，SpEL会使用java.util.Collections.unmodifiableList 方法将列表设置为不可修改。
    Expression exp = parser.parseExpression("{1,2,3}");
    List<Integer> list = exp.getValue(List.class);
    // 等同于如下java代码
    // Integer[] integer = new Integer[]{1,2,3};
    // List<Integer> list = Arrays.asList(integer);
    log.info("{}", list);
    assertEquals(3, list.size());
  }

  @Test
  public void test_collection() {
    String[] arr = new String[]{"a", "b"};

    List<String> list = new ArrayList<>();
    list.add("Hello");
    list.add("World");

    Set<Integer> set = new HashSet<>();
    set.add(1024);
    set.add(2048);
    set.add(3072);

    Map<String, String> map = new HashMap<>();
    map.put("k1", "v1");
    map.put("k2", "v2");

    EvaluationContext context = new StandardEvaluationContext();
    context.setVariable("arr", arr);
    context.setVariable("nullList", null);
    context.setVariable("list", list);
    context.setVariable("set", set);
    context.setVariable("map", map);

    ExpressionParser parser = new SpelExpressionParser();

    // Safe Navigation Operator 安全保证，为了避免操作对象本身可能为null，取属性时报错，可以使用SpEL检验语法。语法： “对象?.变量|方法”
    //log.info("{}", parser.parseExpression("#nullList.length").getValue(context, Integer.class));
    log.info("{}", parser.parseExpression("#nullList?.length").getValue(context, Integer.class));

    // 集合访问。SpEL目前支持所有集合类型和字典类型的元素访问，语法：“集合[索引]”、“map[key]”
    log.info("{}", parser.parseExpression("#arr[0]").getValue(context, String.class));
    log.info("{}", parser.parseExpression("#list[0]").getValue(context, String.class));
    log.info("{}", parser.parseExpression("#set[1]").getValue(context, Integer.class));
    log.info("{}", parser.parseExpression("#map['k2']").getValue(context, String.class));

    // 集合修改。可以使用赋值表达式或Expression接口的setValue方法修改
    log.info("{}", parser.parseExpression("#list[0] = 'Hi'").getValue(context, String.class));
    parser.parseExpression("#map['k2']").setValue(context, "2v");
    log.info("{}", parser.parseExpression("#map['k2']").getValue(context, String.class));

    // 集合选择。通过一定的规则对集合进行筛选，构造出另一个集合。语法：“(list|map).?[选择表达式]”
    // 选择表达式结果必须是boolean类型，如果true则选择的元素将添加到新集合中，false将不添加到新集合中。
    log.info("{}",
        parser.parseExpression("#set.?[#this>2000]").getValue(context, Collection.class));

    // 集合投影。根据集合中的元素中通过选择来构造另一个集合，该集合和原集合具有相同数量的元素。语法：“(list|map).![投影表达式]”
    log.info("{}",
        parser.parseExpression("#set.![#this+1]").getValue(context, Collection.class));
  }

  @Test
  public void test_Collection_Selection() {
    // https://docs.spring.io/spring-framework/reference/core/expressions/language-ref/collection-selection.html
    // Selection uses a syntax of .?[selectionExpression]. 
    // It filters the collection 
    // and returns a new collection that contains a subset of the original elements.

    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);

    //EvaluationContext context = new StandardEvaluationContext();
    EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
    context.setVariable("list", list);
    ExpressionParser parser = new SpelExpressionParser();
    // retrieve the elements that meet the conditions
    log.info("{}",
        parser.parseExpression("#list.?[#this%2==1]").getValue(context, Collection.class));
    // retrieve only the first element
    log.info("{}",
        parser.parseExpression("#list.^[#this%2==1]").getValue(context, Collection.class));
    // retrieve only the last element
    log.info("{}",
        parser.parseExpression("#list.$[#this%2==1]").getValue(context, Collection.class));
  }
}

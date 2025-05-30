package org.example.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * 数据源注解，用于标识方法或类使用的特定数据源。
 * 
 * 该注解可以用于方法或类上，指定使用的数据库类型和名称。
 * 
 * @since 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

  /**
   * 数据源的类型，默认为 "mysql"。
   * 
   * 该属性用于指定数据源的类型，可以是 "mysql"、"oracle"、"postgresql" 等。
   * 
   * @return 数据源类型
   */
  String type() default "mysql";

  /**
   * 数据源的名称，等同于 value 属性。
   * 
   * 在自定义注解中配置具有相同含义但可能不同名称的属性，可以使用别名注解 @AliasFor
   * 
   * @return 数据源名称
   */
  @AliasFor("value")
  String name() default "";

  /**
   * 数据源的名称，等同于 name 属性。
   * 
   * @return 数据源名称
   */
  @AliasFor("name")
  String value() default "";
}
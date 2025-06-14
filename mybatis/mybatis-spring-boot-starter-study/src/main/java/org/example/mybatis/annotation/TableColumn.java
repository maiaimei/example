package org.example.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * Annotation for specifying column details in SQL.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableColumn {
  
  /**
   * Column name in the database
   */
  @AliasFor("value")
  String name();

  /**
   * Column name in the database
   */
  @AliasFor("name")
  String value();

  /**
   * Column type (e.g., JSONB, ARRAY, etc.)
   */
  String type() default "";
}

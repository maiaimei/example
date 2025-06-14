package org.example.mybatis.annotation;

import java.lang.annotation.*;
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
  String name() default "";

  /**
   * Column name in the database
   */
  @AliasFor("name")
  String value() default "";

  /**
   * Column type (e.g., JSONB, ARRAY, etc.)
   */
  String type() default "";
}

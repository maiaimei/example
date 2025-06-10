package org.example.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying column details in SQL.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

  /**
   * Column name in the database
   */
  String name();

  /**
   * Column type (e.g., JSONB, ARRAY, etc.)
   */
  String type() default "";
}

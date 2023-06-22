package cn.maiaimei.example.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import cn.maiaimei.example.validation.constraintvalidators.AlphanumericValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 自定义约束+自定义校验器
 */
@Documented
@Constraint(validatedBy = {AlphanumericValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Alphanumeric {

  String message() default "{cn.maiaimei.example.validation.constraints.Alphanumeric.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int maxlength();

  boolean required() default false;
}

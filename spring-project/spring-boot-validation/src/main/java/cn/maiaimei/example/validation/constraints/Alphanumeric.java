package cn.maiaimei.example.validation.constraints;

import cn.maiaimei.example.validation.constraints.validator.AlphanumericValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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

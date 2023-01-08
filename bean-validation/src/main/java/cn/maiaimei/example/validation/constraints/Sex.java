package cn.maiaimei.example.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {SexValidator.class})
public @interface Sex {
    // 必须的属性
    String message() default "{cn.maiaimei.example.validation.constraints.Sex.message}";

    // 必须的属性
    Class<?>[] groups() default {};

    // 必须的属性
    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}

package cn.maiaimei.example.validation.constraintvalidators;

import cn.maiaimei.example.validation.constraints.Sex;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class SexValidator implements ConstraintValidator<Sex, String> {

  private boolean required;

  @Override
  public void initialize(Sex constraintAnnotation) {
    this.required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return !this.required;
    }
    return "M".equals(value) || "F".equals(value);
  }
}

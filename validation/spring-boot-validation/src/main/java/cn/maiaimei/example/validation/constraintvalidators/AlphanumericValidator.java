package cn.maiaimei.example.validation.constraintvalidators;

import cn.maiaimei.example.validation.constraints.Alphanumeric;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义约束+自定义校验器
 */
public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {

  private static final char[] ALPHANUMERIC = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
      'U', 'V', 'W', 'X', 'Y', 'Z',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z',
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
  };

  private int maxlength;

  private boolean required;

  @Override
  public void initialize(Alphanumeric constraintAnnotation) {
    this.maxlength = constraintAnnotation.maxlength();
    this.required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return !required;
    }
    return value.length() <= maxlength && StringUtils.containsOnly(value, ALPHANUMERIC);
  }
}

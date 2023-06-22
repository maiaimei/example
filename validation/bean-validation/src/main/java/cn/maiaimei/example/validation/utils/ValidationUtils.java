package cn.maiaimei.example.validation.utils;

import cn.maiaimei.example.validation.model.ValidationMessage;
import cn.maiaimei.example.validation.model.ValidationResult;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.spi.ValidationProvider;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

/**
 * <p>使用SPI机制{@link
 * Validation.GetValidationProviderListAction#loadProviders(java.lang.ClassLoader)}获取{@link
 * ValidationProvider}的实现类{@link HibernateValidator}</p>
 *
 * <p>真正执行校验工作的是{@link ValidatorImpl}</p>
 *
 * <p>约束注解的校验类需要实现{@link
 * ConstraintValidator}接口，如：NotNullValidator完成@NotNull注解的校验，即XxxValidator完成@Xxx注解的校验</p>
 *
 * <p>在{@link ConstraintHelper}中配置@Xxx注解与XxxValidator校验器的绑定关系</p>
 */
public class ValidationUtils {

  private static final Validator VALIDATOR;
  private static final Validator FAIL_FAST_VALIDATOR;
  private static final ExecutableValidator EXECUTABLE_VALIDATOR;
  private static final ExecutableValidator FAIL_FAST_EXECUTABLE_VALIDATOR;

  static {
    // 默认Validator
    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    // 快速失败Validator
    FAIL_FAST_VALIDATOR = Validation.byProvider(HibernateValidator.class)
        .configure().failFast(Boolean.TRUE)
        .buildValidatorFactory().getValidator();
    // non-bean Validator，即简单类型入参或返回值校验
    EXECUTABLE_VALIDATOR = VALIDATOR.forExecutables();
    // non-bean 快速失败Validator，即简单类型入参或返回值校验
    FAIL_FAST_EXECUTABLE_VALIDATOR = FAIL_FAST_VALIDATOR.forExecutables();
  }

  public static <T> ValidationResult validate(T object, Class<?>... groups) {
    // 若省略groups，默认校验组是 javax.validation.groups.Default
    Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object, groups);
    return checkConstraintViolations(constraintViolations);
  }

  /**
   * non-bean（简单类型）入参校验
   */
  public static <T> ValidationResult validateParameters(T object,
      Method method,
      Object[] parameterValues,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateParameters(
        object, method, parameterValues, groups);
    return checkConstraintViolations(constraintViolations);
  }

  /**
   * non-bean（简单类型）返回值校验
   */
  public static <T> ValidationResult validateReturnValue(T object,
      Method method,
      Object returnValue,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateReturnValue(
        object, method, returnValue, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public static <T> ValidationResult failFastValidate(T object, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_VALIDATOR.validate(object, groups);
    return checkConstraintViolations(constraintViolations);
  }

  /**
   * non-bean（简单类型）入参校验
   */
  public static <T> ValidationResult failFastValidateParameters(T object,
      Method method,
      Object[] parameterValues,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_EXECUTABLE_VALIDATOR.validateParameters(
        object, method, parameterValues, groups);
    return checkConstraintViolations(constraintViolations);
  }

  /**
   * non-bean（简单类型）返回值校验
   */
  public static <T> ValidationResult failFastValidateReturnValue(T object,
      Method method,
      Object returnValue,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_EXECUTABLE_VALIDATOR.validateReturnValue(
        object, method, returnValue, groups);
    return checkConstraintViolations(constraintViolations);
  }

  private static <T> ValidationResult checkConstraintViolations(
      Set<ConstraintViolation<T>> constraintViolations) {
    ValidationResult validationResult = new ValidationResult();
    if (Objects.nonNull(constraintViolations) && !constraintViolations.isEmpty()) {
      constraintViolations
          .forEach(cv -> {
            final ValidationMessage validationMessage = new ValidationMessage();
            validationMessage.setCode(cv.getPropertyPath().toString());
            validationMessage.setDescription(cv.getMessage());
            validationMessage.setValue(cv.getInvalidValue());
            validationResult.addError(validationMessage);
          });
    }
    return validationResult;
  }

  private static Validator getValidator(String language) {
    Locale.setDefault(new Locale(language));
    return Validation.byDefaultProvider().configure()
        // 自定义国际化文件配置
        .messageInterpolator(new ValidationResourceBundleMessageInterpolator(
            new PlatformResourceBundleLocator("i18n/ValidationMessages")))
        .buildValidatorFactory()
        .getValidator();
  }

  /**
   * java中properties配置文件默认的编码为：ISO-8859-1，是不支持中文的，所以会乱码，需要做转码
   */
  private static class ValidationResourceBundleMessageInterpolator extends
      ResourceBundleMessageInterpolator {

    public ValidationResourceBundleMessageInterpolator(
        ResourceBundleLocator userResourceBundleLocator) {
      super(userResourceBundleLocator);
    }

    @Override
    public String interpolate(String message, Context context) {
      String result = super.interpolate(message, context);
      try {
        return new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
      } catch (Exception e) {
        return result;
      }
    }
  }
}

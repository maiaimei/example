package cn.maiaimei.example.util;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {
    private static final String ERROR_MESSAGE_FORMAT = "%s %s, invalid value: %s";
    /**
     * validator加载原理
     * {@link Validation.GetValidationProviderListAction#loadProviders(java.lang.ClassLoader)}
     * {@link HibernateValidator}
     * {@link ValidatorImpl}
     * {@link ConstraintValidator}
     * {@link ConstraintHelper}
     */
    private static final Validator VALIDATOR;
    private static final Validator FAIL_FAST_VALIDATOR;
    private static final ExecutableValidator EXECUTABLE_VALIDATOR;
    private static final ExecutableValidator FAIL_FAST_EXECUTABLE_VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
        // 快速失败
        FAIL_FAST_VALIDATOR = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(Boolean.TRUE)
                .buildValidatorFactory().getValidator();
        // non-bean（简单类型）入参/返回值校验
        EXECUTABLE_VALIDATOR = VALIDATOR.forExecutables();
        FAIL_FAST_EXECUTABLE_VALIDATOR = FAIL_FAST_VALIDATOR.forExecutables();
    }

    public static <T> ValidationResult valid(T object) {
        // 默认校验组是 javax.validation.groups.Default
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);
        return checkConstraintViolations(constraintViolations);
    }

    public static <T> ValidationResult valid(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object, groups);
        return checkConstraintViolations(constraintViolations);
    }

    public static <T> ValidationResult validFailFast(T object) {
        // 默认校验组是 javax.validation.groups.Default
        Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_VALIDATOR.validate(object);
        return checkConstraintViolations(constraintViolations);
    }

    public static <T> ValidationResult validFailFast(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_VALIDATOR.validate(object, groups);
        return checkConstraintViolations(constraintViolations);
    }

    /**
     * non-bean（简单类型）入参校验
     */
    public static <T> ValidationResult validateParameters(T object,
                                                          Method method,
                                                          Object[] parameterValues,
                                                          Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateParameters(object, method, parameterValues, groups);
        return checkConstraintViolations(constraintViolations);
    }

    /**
     * non-bean（简单类型）返回值校验
     */
    public static <T> ValidationResult validateReturnValue(T object,
                                                           Method method,
                                                           Object returnValue,
                                                           Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateReturnValue(object, method, returnValue, groups);
        return checkConstraintViolations(constraintViolations);
    }

    private static <T> ValidationResult checkConstraintViolations(Set<ConstraintViolation<T>> constraintViolations) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setHasErrors(Boolean.FALSE);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            List<String> errorMessages = constraintViolations.stream()
                    .map(cv -> String.format(ERROR_MESSAGE_FORMAT, cv.getPropertyPath().toString(), cv.getMessage(), cv.getInvalidValue()))
                    .collect(Collectors.toList());
            validationResult.setHasErrors(Boolean.TRUE);
            validationResult.setErrorMessages(errorMessages);
        }
        return validationResult;
    }
}

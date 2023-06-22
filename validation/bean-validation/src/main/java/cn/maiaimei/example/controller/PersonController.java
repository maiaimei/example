package cn.maiaimei.example.controller;

import cn.maiaimei.example.validation.model.ValidationResult;
import cn.maiaimei.example.validation.utils.ValidationUtils;
import java.lang.reflect.Method;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonController {

  public void delete(@NotNull Long id) {
    // non-bean（简单类型）入参/返回值校验需要结合AOP一起使用！
    StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
    String methodName = stackTraceElement.getMethodName();
    Method method = null;
    try {
      // 如何获取参数类型？
      method = this.getClass().getDeclaredMethod(methodName, Long.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    ValidationResult result = ValidationUtils.validateParameters(this, method, new Object[]{id});
    if (result.hasErrors()) {
      result.getAllErrors().forEach(validationMessage -> {
        log.info("Error Code: {}", validationMessage.getCode());
        log.info("Error Message: {}", validationMessage.getDescription());
        log.info("Error Value: {}", validationMessage.getValue());
      });
    }
  }
}

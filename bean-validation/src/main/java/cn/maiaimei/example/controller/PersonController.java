package cn.maiaimei.example.controller;

import cn.maiaimei.example.util.ValidationResult;
import cn.maiaimei.example.util.ValidationUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

public class PersonController {
    public void delete(@NotNull Long id) {
        // non-bean（简单类型）入参/返回值校验需要结合AOP一起使用！
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        String methodName = stackTraceElement.getMethodName();
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(methodName, Long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ValidationResult result = ValidationUtils.validateParameters(this, method, new Object[]{id});
        if (result.isHasErrors()) {
            result.getErrorMessages().forEach(System.out::println);
        }
    }
}

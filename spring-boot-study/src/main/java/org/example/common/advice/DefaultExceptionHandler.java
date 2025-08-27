package org.example.common.advice;

import cn.maiaimei.advice.BaseExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
    "org.example.common.controller"
})
public class DefaultExceptionHandler extends BaseExceptionHandler {

}

package org.example.advice;

import cn.maiaimei.advice.BaseExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
    "org.example.controller"
})
public class DefaultExceptionHandler extends BaseExceptionHandler {

}

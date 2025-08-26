package org.example.advice;

import cn.maiaimei.advice.BaseResponseHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
    "org.example.controller"
})
public class DefaultResponseHandler extends BaseResponseHandler {

}

package org.example.common.advice;

import cn.maiaimei.advice.BaseResponseHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
    "org.example.common.controller"
})
public class DefaultResponseHandler extends BaseResponseHandler {

}

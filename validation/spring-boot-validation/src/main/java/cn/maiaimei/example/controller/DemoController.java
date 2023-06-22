package cn.maiaimei.example.controller;

import cn.maiaimei.example.dto.UserRequest;
import cn.maiaimei.example.validation.constraints.Alphanumeric;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@Slf4j
@Api
@RestController
@RequestMapping("/demos")
public class DemoController {
    @ApiOperation(value = "测试@Alphanumeric注解")
    @GetMapping("/testAlphanumeric")
    public void testAlphanumeric(@Alphanumeric(maxlength = 3, required = true) @RequestParam(required = false) String value) {
        log.info("value={}", value);
    }

    @ApiOperation(value = "测试@Valid注解")
    @PostMapping("/testValid")
    public String testValid(@Valid @RequestBody UserRequest request, BindingResult bindingResult) {
        log.info("request: {}", request);
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(objectError -> log.info("{} {}", objectError.getObjectName(), objectError.getDefaultMessage()));
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> log.info("{} {}, invalid value: {}", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
        }
        return bindingResult.hasErrors() ? "参数校验失败" : "参数校验成功";
    }

    @ApiOperation(value = "测试@Validated注解")
    @PostMapping("/testValidated")
    public String testValidated(@Validated(value = {ValidationGroup.CheckInOrder.class}) @RequestBody UserRequest request, BindingResult bindingResult) {
        log.info("request: {}", request);
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(objectError -> log.info("{} {}", objectError.getObjectName(), objectError.getDefaultMessage()));
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> log.info("{} {}, invalid value: {}", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
        }
        return bindingResult.hasErrors() ? "参数校验失败" : "参数校验成功";
    }
}

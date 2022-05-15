package cn.maiaimei.example.controller;

import cn.maiaimei.framework.exception.BusinessException;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "接口返回值统一处理")
@RestController
@RequestMapping("/returntype")
public class ReturnTypeController {
    @GetMapping("/void")
    public void returnVoid() {
    }

    @GetMapping("/string")
    public String returnString() {
        return "string";
    }

    @GetMapping("/integer")
    public Integer returnInteger() {
        return 0;
    }

    @GetMapping("/double")
    public Double returnDouble() {
        return 3.14;
    }

    @GetMapping("/boolean")
    public Boolean returnBoolean() {
        return true;
    }

    @GetMapping("/map")
    public Map returnMap() {
        HashMap map = new HashMap();
        map.put("k1", "v1");
        map.put("k2", "v2");
        return map;
    }

    @GetMapping("/array")
    public int[] returnArray() {
        return new int[]{1, 2, 3};
    }

    @GetMapping("/list")
    public List returnList() {
        return Arrays.asList(1, 2, 3);
    }

    @GetMapping("/runtime/exception")
    public void throwRuntimeException() {
        int m = 1;
        int n = 0;
        int result = m / n;
    }

    @GetMapping("/business/exception")
    public void throwBusinessException() {
        throw new BusinessException("BusinessException");
    }
}

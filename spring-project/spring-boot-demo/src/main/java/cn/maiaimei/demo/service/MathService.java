package cn.maiaimei.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathService {
    private static final Logger log = LoggerFactory.getLogger(MathService.class);

    public Integer div(int i, int j) {
        log.info("执行目标方法");
        return i / j;
    }
}

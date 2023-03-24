package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @GetMapping("/trace")
    public String trace() {
        log.trace("===> trace 1");
        if (log.isTraceEnabled()) {
            log.trace("===> trace 2");
        }
        return logService.trace();
    }

    @GetMapping("/debug")
    public String debug() {
        log.debug("===> debug 1");
        if (log.isDebugEnabled()) {
            log.debug("===> debug 2");
        }
        return logService.debug();
    }

    @GetMapping("/info")
    public String info() {
        log.info("===> info 1");
        if (log.isInfoEnabled()) {
            log.info("===> info 2");
        }
        return logService.info();
    }

    @GetMapping("/warn")
    public String warn() {
        log.warn("===> warn 1");
        if (log.isWarnEnabled()) {
            log.warn("===> warn 2");
        }
        return logService.warn();
    }

    @GetMapping("/error")
    public String error() {
        log.error("===> error 1");
        if (log.isErrorEnabled()) {
            log.error("===> error 2");
        }
        return logService.error();
    }
}

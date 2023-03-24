package cn.maiaimei.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    public String trace() {
        log.trace("<=== trace 1");
        if (log.isTraceEnabled()) {
            log.trace("<=== trace 2");
        }
        return "trace";
    }

    public String debug() {
        log.debug("<=== debug 1");
        if (log.isDebugEnabled()) {
            log.debug("<=== debug 2");
        }
        return "debug";
    }

    public String info() {
        log.info("<=== info 1");
        if (log.isInfoEnabled()) {
            log.info("<=== info 2");
        }
        return "info";
    }

    public String warn() {
        log.warn("<=== warn 1");
        if (log.isWarnEnabled()) {
            log.warn("<=== warn 2");
        }
        return "warn";
    }

    public String error() {
        log.error("<=== error 1");
        if (log.isErrorEnabled()) {
            log.error("<=== error 2");
        }
        return "error";
    }
}

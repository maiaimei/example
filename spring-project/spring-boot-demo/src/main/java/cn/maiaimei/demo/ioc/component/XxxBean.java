package cn.maiaimei.demo.ioc.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class XxxBean implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(XxxBean.class);

    private YyyBean yyyBean;

    public YyyBean getYyyBean() {
        return yyyBean;
    }

    @Autowired
    public void setYyyBean(YyyBean yyyBean) {
        log.info("XxxBean populateBean");
        this.yyyBean = yyyBean;
    }

    public XxxBean() {
        log.info("XxxBean Constructor");
    }

    public void initMethod() {
        log.info("XxxBean initMethod");
    }

    public void destroyMethod() {
        log.info("XxxBean destroyMethod");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("XxxBean InitializingBean.afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception {
        log.info("XxxBean DisposableBean.destroy");
    }

    @PostConstruct
    public void init() {
        log.info("XxxBean @PostConstruct");
    }

    @PreDestroy
    public void clear() {
        log.info("XxxBean @PreDestroy");
    }

}

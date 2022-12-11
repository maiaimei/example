package cn.maiaimei.demo.color;

import org.springframework.beans.factory.FactoryBean;

public class PinkFactoryBean implements FactoryBean<Pink> {
    @Override
    public Pink getObject() throws Exception {
        return new Pink();
    }

    @Override
    public Class<?> getObjectType() {
        return Pink.class;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }
}

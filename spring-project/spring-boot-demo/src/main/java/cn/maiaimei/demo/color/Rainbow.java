package cn.maiaimei.demo.color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * 依赖注入
 * {@link AutowiredAnnotationBeanPostProcessor}
 * {@link CommonAnnotationBeanPostProcessor}
 *
 * <p> @Autowired ：
 * 先按类型注入，
 * 如果同一类型存在多个实例，优先注入标记@Primary的，
 * 如果没有标记@Primary的，尝试按名称注入，可结合@Qualifier使用，
 * 如果没有找到Bean，则抛出NoSuchBeanDefinitionException，可以通过@Autowired(required = false)解决
 *
 * <p> @Resource ： JSR-250规范
 *
 * <p> @Inject ：JSR-330规范，需要导入javax.inject包
 */
public class Rainbow {
    private Yellow yellow;
    private Yellow huangse;
    private Yellow huang;

    public Yellow getYellow() {
        return yellow;
    }

    /**
     * {@link AutowiredAnnotationBeanPostProcessor#postProcessProperties(org.springframework.beans.PropertyValues, java.lang.Object, java.lang.String)}
     */
    @Autowired
    public void setYellow(Yellow yellow) {
        this.yellow = yellow;
    }

    public Yellow getHuangse() {
        return huangse;
    }

    /**
     * {@link CommonAnnotationBeanPostProcessor#postProcessProperties(org.springframework.beans.PropertyValues, java.lang.Object, java.lang.String)}
     */
    @Resource
    public void setHuangse(Yellow huangse) {
        this.huangse = huangse;
    }

    public Yellow getHuang() {
        return huang;
    }

    /**
     * {@link AutowiredAnnotationBeanPostProcessor#postProcessProperties(org.springframework.beans.PropertyValues, java.lang.Object, java.lang.String)}
     */
    @Inject
    public void setHuang(Yellow huang) {
        this.huang = huang;
    }

    @Autowired
    private Golden golden;

    @Autowired
    @Qualifier("golden")
    private Golden jin;

    @Autowired(required = false)
    private Gray gray;

    //@Resource // NoSuchBeanDefinitionException
    private Gray hui;

    //@Inject // NoSuchBeanDefinitionException
    private Gray huise;

    @Resource
    private Green green;

    @Resource
    private Green lv;

    @Inject
    private Orange orange;

    @Inject
    private Orange cheng;

    @Inject
    @Named("orange")
    private Object chengse;

    @Override
    public String toString() {
        return "Rainbow{" +
                "yellow=" + yellow +
                ", huangse=" + huangse +
                ", huang=" + huang +
                ", golden=" + golden +
                ", jin=" + jin +
                ", gray=" + gray +
                ", hui=" + hui +
                ", huise=" + huise +
                ", green=" + green +
                ", lv=" + lv +
                ", orange=" + orange +
                ", cheng=" + cheng +
                ", chengse=" + chengse +
                '}';
    }
}

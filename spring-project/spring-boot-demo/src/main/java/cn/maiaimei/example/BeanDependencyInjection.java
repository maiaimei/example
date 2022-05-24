package cn.maiaimei.example;

import cn.maiaimei.example.beans.AaaComponent;
import cn.maiaimei.example.beans.DddComponent;
import cn.maiaimei.example.beans.MyRepository;
import cn.maiaimei.example.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@Component
public class BeanDependencyInjection {
    private AaaComponent aaaComponent;
    private MyRepository myRepository;

    /**
     * 属性注入 - @Autowired
     */
    @Autowired
    private DddComponent dddComponent;

    /**
     * 属性注入 - @Autowired, byType自动注入
     */
    @Autowired
    private DddComponent dddComponent1;

    /**
     * 属性注入 - @Autowired, byType自动注入
     * 在根据类型进行自动装配时，如果该类型存在多个不同名称的Bean，则会按照名称进行匹配，如果属性名称与Bean名称相同则装配成功，否则失败。
     * 如果需要按指定名称进行装配，则需要配合@Qualifier注解，可以解决“Could not autowire. There is more than one bean of 'Xxx' type.”
     */
    @Autowired
    private IService xxxService;

    @Autowired
    @Qualifier("xxxService")
    private IService xxxService2;

    @Resource
    private IService yyyService;

    @Inject
    @Named("yyyService")
    private IService yyyService2;

    /**
     * Setter方法注入
     *
     * @param aaaComponent
     */
    @Autowired
    public void setAaaComponent(AaaComponent aaaComponent) {
        this.aaaComponent = aaaComponent;
    }

    /**
     * 构造器注入
     *
     * @param myRepository
     */
    @Autowired
    public BeanDependencyInjection(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    public void showMember() {
        log.info("{}", myRepository);
        log.info("{}", aaaComponent);
        log.info("{}", dddComponent);
        log.info("{}", dddComponent1);
        log.info("{}", xxxService);
        log.info("{}", xxxService2);
        log.info("{}", yyyService);
        log.info("{}", yyyService2);
    }
}

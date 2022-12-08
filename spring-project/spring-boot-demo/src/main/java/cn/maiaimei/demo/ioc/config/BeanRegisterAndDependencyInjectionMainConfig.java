package cn.maiaimei.demo.ioc.config;

import cn.maiaimei.demo.ioc.color.Rainbow;
import org.springframework.context.annotation.*;

/**
 * Bean注册和依赖注入
 */
@ComponentScan(
        basePackages = {"cn.maiaimei.demo.ioc"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanLifeCycleMainConfig.class)}
)
@PropertySource("classpath:/db.properties")
@Configuration
public class BeanRegisterAndDependencyInjectionMainConfig {
    /**
     * 依赖注入 / Dependency injection / DI
     *
     * @Autowired, @Resource, @Inject
     */
    @Bean
    public Rainbow rainbow() {
        return new Rainbow();
    }
}

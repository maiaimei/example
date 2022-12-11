package cn.maiaimei.demo;

import cn.maiaimei.demo.color.Rainbow;
import cn.maiaimei.demo.properties.DbProperties;
import org.springframework.context.annotation.*;

/**
 * Bean注册和依赖注入
 */
@ComponentScan(basePackages = {
        "cn.maiaimei.demo.controller",
        "cn.maiaimei.demo.service",
        "cn.maiaimei.demo.repository",
        "cn.maiaimei.demo.component",
        "cn.maiaimei.demo.color"
})
@PropertySource("classpath:/db.properties")
@Import(value = DbProperties.class)
@Configuration
public class MainConfig {
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

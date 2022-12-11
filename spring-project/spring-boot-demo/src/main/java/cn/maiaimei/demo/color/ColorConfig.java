package cn.maiaimei.demo.color;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(value = {
        // 导入普通类注册Bean
        Green.class,
        // 实现ImportSelector接口注册Bean
        ColorImportSelector.class,
        // 实现ImportBeanDefinitionRegistrar接口注册Bean
        ColorImportBeanDefinitionRegistrar.class,
        // 实现BeanDefinitionRegistryPostProcessor接口注册Bean
        ColorBeanDefinitionRegistryPostProcessor.class
})
// 导入XML注册Bean
@ImportResource("classpath:/beans.xml")
public class ColorConfig {
    @Bean
    public Red red() {
        return new Red();
    }

    /**
     * 实现FactoryBean注册Bean
     *
     * @return
     */
    @Bean
    public PinkFactoryBean pinkFactoryBean() {
        return new PinkFactoryBean();
    }
}

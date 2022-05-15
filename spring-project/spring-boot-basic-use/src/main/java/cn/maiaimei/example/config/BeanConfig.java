package cn.maiaimei.example.config;

import cn.maiaimei.example.BeanLifeCycle;
import cn.maiaimei.example.beans.AaaComponent;
import cn.maiaimei.example.beans.BbbComponent;
import cn.maiaimei.example.beans.CccComponent;
import cn.maiaimei.example.beans.DddComponent;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@Import(value = {
        AaaComponent.class,
        BeanConfig.BeanConfigImportSelector.class,
        BeanConfig.BeanConfigImportBeanDefinitionRegistrar.class
})
public class BeanConfig {
    //@Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public BeanLifeCycle beanLifeCycle() {
        return new BeanLifeCycle();
    }

    @Bean
    public DddComponent dddComponent() {
        return new DddComponent();
    }

    static class BeanConfigImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{
                    "cn.maiaimei.example.beans.EeeComponent",
                    "cn.maiaimei.example.beans.FffComponent"
            };
        }
    }

    static class BeanConfigImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            RootBeanDefinition bbbComponentDefinition = new RootBeanDefinition(BbbComponent.class);
            RootBeanDefinition cccComponentDefinition = new RootBeanDefinition(CccComponent.class);
            registry.registerBeanDefinition("bbbComponent", bbbComponentDefinition);
            registry.registerBeanDefinition("cccComponent", cccComponentDefinition);
        }
    }
}

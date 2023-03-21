package cn.maiaimei.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class FreemarkerConfig {

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() {
        final freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "templates");
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return configuration;
    }
}

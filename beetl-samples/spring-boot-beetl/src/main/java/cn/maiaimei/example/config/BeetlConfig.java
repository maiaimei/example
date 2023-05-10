package cn.maiaimei.example.config;

import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;

@Configuration
public class BeetlConfig {
    //@Bean
    public BeetlGroupUtilConfiguration beetlGroupUtilConfiguration() throws IOException {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        // 默认根路径为resources/templates文件夹。设置根路径为resources文件夹
        WebAppResourceLoader webAppResourceLoader =
                new WebAppResourceLoader(patternResolver.getResource("classpath:/").getFile().getPath());
        beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
        return beetlGroupUtilConfiguration;
    }

    /**
     * 假设项目Controller代码中 return "add";
     * 那么跳转的页面即为/resources/WEB-INF/views/add.html
     */
    //@Bean
    public BeetlSpringViewResolver beetlSpringViewResolver(BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setPrefix("WEB-INF/views/");
        // 默认后缀为btl。设置后缀为html
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }
}

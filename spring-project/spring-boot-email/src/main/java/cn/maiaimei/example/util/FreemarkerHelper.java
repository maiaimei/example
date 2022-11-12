package cn.maiaimei.example.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;

@Component
public class FreemarkerHelper {
    /**
     * 自动注入FreeMarker配置类,用户获取模板
     */
    @Resource
    private Configuration configuration;

    @SneakyThrows
    public String processTemplateIntoString(String templateName, Object model) {
        Template template = configuration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}

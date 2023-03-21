package cn.maiaimei.example.engine;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

@Component
public class FreemarkerEngine {

    private final Configuration freemarkerConfiguration;

    @Autowired
    public FreemarkerEngine(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    @SneakyThrows
    public String process(String templateName, Object dataModel) {
        final Template template = freemarkerConfiguration.getTemplate(templateName);
        StringWriter stringWriter = new StringWriter();
        template.process(dataModel, stringWriter);
        return stringWriter.toString();
    }
}

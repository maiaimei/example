package cn.maiaimei.example.engine;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

@Component
public class FreemarkerEngine {

    private final Configuration freemarkerConfiguration;

    @Autowired
    public FreemarkerEngine(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    @SneakyThrows
    public String render(String templateName, Object dataModel) {
        final Template template = freemarkerConfiguration.getTemplate(templateName);
        StringWriter stringWriter = new StringWriter();
        template.process(dataModel, stringWriter);
        return stringWriter.toString();
    }

    @SneakyThrows
    public void writeToConsole(String templateName, Object dataModel) {
        Template template = freemarkerConfiguration.getTemplate(templateName);
        Writer out = new OutputStreamWriter(System.out);
        template.process(dataModel, out);
    }

    @SneakyThrows
    public void writeToFile(String pathname, String templateName, Object dataModel) {
        final Template template = freemarkerConfiguration.getTemplate(templateName);
        Writer out = new OutputStreamWriter(new FileOutputStream(pathname));
        template.process(dataModel, out);
    }

}

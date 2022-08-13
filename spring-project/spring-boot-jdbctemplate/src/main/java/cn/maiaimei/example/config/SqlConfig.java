package cn.maiaimei.example.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SqlConfig {
    public static final Map<String, String> USER_SQLS = new HashMap<>();

    @PostConstruct
    public void init() {
        List<Sqls.Sql> userConfig = getSqlConfig("mapper/user.xml").getSqls();

        userConfig.stream().forEach(cfg -> {
            USER_SQLS.put(cfg.getKey(), replaceAllBlank(cfg.getValue()));
        });
    }

    @SneakyThrows
    private Sqls getSqlConfig(String path) {
        JAXBContext context = JAXBContext.newInstance(Sqls.class);
        Unmarshaller u = context.createUnmarshaller();
        ClassPathResource classPathResource = new ClassPathResource(path);
        InputStream inputStream = classPathResource.getInputStream();
        return (Sqls) u.unmarshal(inputStream);
    }

    private String replaceAllBlank(String value) {
        return value.replaceAll("[\n\r]", "").replaceAll("\\s+", " ").trim();
    }
}
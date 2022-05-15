package cn.maiaimei.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "springfox.documentation.swagger-ui")
public class SwaggerUiConfigProperties {
    private Boolean enabled;
    private String title;
    private String description;
    private String version;
}

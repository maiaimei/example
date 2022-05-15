package cn.maiaimei.example.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnProperty(value = "springfox.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(SwaggerUiConfigProperties.class)
public class SwaggerConfig {
    private SwaggerUiConfigProperties swaggerUiConfigProperties;

    public SwaggerConfig(SwaggerUiConfigProperties swaggerProperties) {
        this.swaggerUiConfigProperties = swaggerProperties;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.any())
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build()
                .enable(swaggerUiConfigProperties.getEnabled());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerUiConfigProperties.getTitle())
                .description(swaggerUiConfigProperties.getDescription())
                .version(swaggerUiConfigProperties.getVersion())
                .build();
    }
}

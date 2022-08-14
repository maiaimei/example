package cn.maiaimei.example.component;

import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "springfox.enabled", havingValue = "true")
public class SwaggerConfig {
    private SwaggerProperties swaggerProperties;

    @Autowired
    public void setSwaggerProperties(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build()
                //.globalRequestParameters(globalRequestParameters())
                .enable(swaggerProperties.getEnabled());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .build();
    }

    private List<RequestParameter> globalRequestParameters() {
        RequestParameterBuilder parameterBuilder1 = new RequestParameterBuilder()
                // 每次请求加载header
                .in(ParameterType.HEADER)
                // 头标签
                .name("X-Test-Id")
                .required(false)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));

        RequestParameterBuilder parameterBuilder2 = new RequestParameterBuilder()
                // 每次请求加载header
                .in(ParameterType.HEADER)
                // 头标签
                .name("X-Test-Name")
                .required(false)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));

        return Arrays.asList(parameterBuilder1.build(), parameterBuilder2.build());
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "springfox.documentation.swagger-ui")
    public static class SwaggerProperties {
        private Boolean enabled;
        private String title;
        private String description;
        private String version;
    }
}
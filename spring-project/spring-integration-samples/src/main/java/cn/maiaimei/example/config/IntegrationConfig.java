package cn.maiaimei.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

/**
 * https://docs.spring.io/spring-integration/docs/current/reference/html/overview.html#configuration-enable-integration
 * The @IntegrationComponentScan annotation also permits classpath scanning. This annotation plays a similar role as the standard Spring Framework @ComponentScan annotation, but it is restricted to components and annotations that are specific to Spring Integration, which the standard Spring Framework component scan mechanism cannot reach. For an example, see @MessagingGateway Annotation.
 */
@Configuration
@ImportResource("classpath:beans/*.xml")
@EnableIntegration
@IntegrationComponentScan("cn.maiaimei.example")
public class IntegrationConfig {
}

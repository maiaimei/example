# Spring Security

[Spring Security](https://docs.spring.io/spring-security/reference/index.html)是一个安全框架，核心功能包括用户认证（Authentication）和用户授权（Authorization）。

认证流程通过过滤器链来实现。当用户发送请求时，Spring Security会通过一系列过滤器来拦截请求，并进行身份验证。

授权流程通过 `http.authorizeRequests()`对web请求进行授权保护。授权决策由 `AccessDecisionManager` 进行，它会对比当前访问资源所需的权限信息和用户信息中的权限信息。

How Spring Security works, you can refer to the [Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html) section.

## How spring-boot-starter-security works

Add `spring-boot-starter-security` in pom.xml

Beans related to Spring Security will be automatically registered, refer to `org.springframework.boot.autoconfigure.AutoConfiguration.imports` in spring-boot-autoconfigure.jar, search

`org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration`

Key code:

```java
@Import({ SpringBootWebSecurityConfiguration.class, SecurityDataConfiguration.class })
public class SecurityAutoConfiguration {}

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
class SpringBootWebSecurityConfiguration {
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnDefaultWebSecurity
	static class SecurityFilterChainConfiguration {

		@Bean
		@Order(SecurityProperties.BASIC_AUTH_ORDER)
		SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
			http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
			http.formLogin(withDefaults());
			http.httpBasic(withDefaults());
			return http.build();
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(name = BeanIds.SPRING_SECURITY_FILTER_CHAIN)
	@ConditionalOnClass(EnableWebSecurity.class)
	@EnableWebSecurity
	static class WebSecurityEnablerConfiguration {

	}
}
```

`HttpSecurity` bean is registered in `org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration`




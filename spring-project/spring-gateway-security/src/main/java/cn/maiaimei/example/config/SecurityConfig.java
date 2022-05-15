package cn.maiaimei.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * https://www.cnblogs.com/wgslucky/p/11962884.html
 */
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        RedirectServerAuthenticationEntryPoint loginPoint = new RedirectServerAuthenticationEntryPoint("/xinyue-server-a/account/index");
//        http.authorizeExchange().pathMatchers("/xinyue-server-a/easyui/**", "/xinyue-server-a/js/**", "/xinyue-server-a/account/index", "/xinyue-server-a/account/login").permitAll()
//                .and().formLogin().loginPage("/xinyue-server-a/account/authen").authenticationEntryPoint(loginPoint)
//                .and().authorizeExchange().anyExchange().authenticated()
//                .and().csrf().disable();

        http.formLogin();
        http.authorizeExchange().pathMatchers("/login").permitAll()
                .and().authorizeExchange().anyExchange().authenticated()
                .and().csrf().disable();
        
        SecurityWebFilterChain chain = http.build();
        return chain;
    }
}
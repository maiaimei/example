package cn.maiaimei.example.config;

import cn.maiaimei.example.filter.PreAuthFilter;
import cn.maiaimei.example.handler.CustomAccessDeniedHandler;
import cn.maiaimei.example.handler.CustomAuthenticationSuccessHandler;
import cn.maiaimei.example.handler.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;

/**
 * <p>
 * Spring Security 在 Servlet 的过滤链（filter chain）中注册了一个过滤器 FilterChainProxy，
 * 它会把请求代理到 Spring Security 自己维护的多个过滤链，每个过滤链会匹配一些 URL，如果匹配则执行对应的过滤器。
 * 过滤链是有顺序的，一个请求只会执行第一条匹配的过滤链。
 * Spring Security 的配置本质上就是新增、删除、修改过滤器。
 * {@link DelegatingFilterProxy}
 * {@link FilterChainProxy#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * {@link FilterChainProxy#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * {@link DefaultSecurityFilterChain#getFilters()} -> 获取Spring Security自己维护的多个过滤链
 * {@link FilterChainProxy.VirtualFilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)} -> 挨个执行Spring Security的过滤链：nextFilter.doFilter(request, response, this);
 * </p>
 * <p>
 * 身份认证
 * {@link AbstractAuthenticationProcessingFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * {@link UsernamePasswordAuthenticationFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * {@link ProviderManager#authenticate(org.springframework.security.core.Authentication)}
 * {@link AbstractUserDetailsAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)}
 * {@link DaoAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)}
 * {@link UserDetailsService#loadUserByUsername(java.lang.String)}
 * </p>
 * <p>
 * 注销
 * {@link LogoutFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * {@link CompositeLogoutHandler#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)}
 * </p>
 * <p>
 * {@link SecurityContextHolder}
 * </p>
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private PreAuthFilter preAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 记住我
     *
     * @return
     */
    //@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动时自动建表，以后启动要注释该行代码，否则会报错
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    //@Bean
    //public HttpSessionIdResolver httpSessionIdResolver() {
    //    return HeaderHttpSessionIdResolver.xAuthToken();
    //}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 放行静态资源
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/favicon.ico",
                "/*.html",
                "/**/*.html",
                "/h2-console/**",
                "/resources/**",
                "/webjars/**",
                "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 登录
        http.formLogin().permitAll().successHandler(customAuthenticationSuccessHandler)
                // 注销
                .and().logout().logoutSuccessUrl("/login").addLogoutHandler(customLogoutHandler)
                // 授权认证
                .and().authorizeRequests().anyRequest().authenticated()
                // 禁用session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 允许来自同一来源的 H2 控制台的请求
                .and().headers().frameOptions().sameOrigin()
                // 禁用csrf
                .and().csrf().disable();

        // 异常处理
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        // 添加过滤器
        http.addFilterBefore(preAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private void configureLoginDemo(HttpSecurity http) throws Exception {
        http.formLogin()
                // 配置未登录时地址，需要写相应方法，默认是 login
                .loginPage("/toLogin")
                // 配置用户名参数，默认是 username
                .usernameParameter("username")
                // 配置密码参数，默认是 password
                .passwordParameter("password")
                // 配置登录处理地址，这里仅做配置，无需写相应方法，只需要保证前端提交登录请求的地址与此一致即可，默认是 login
                // 登录成功返回 /
                // 登录失败返回 /xxx?error，其中xxx为loginPage()配置的地址
                //.loginProcessingUrl("/doLogin")
                // 配置登录成功后请求转发的地址，POST请求。刷新页面可能出现重复提交表单的情况
                //.successForwardUrl("/toMain")
                // 配置登录成功后响应重定向的地址，GET请求。
                //.defaultSuccessUrl("/toMain")
                // 自定义请求处理逻辑
                .successHandler(customAuthenticationSuccessHandler)
                .failureForwardUrl("")
                .failureUrl("")
                .failureHandler(null);
    }

    private void configureLogoutDemo(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("")
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler(null)
                .addLogoutHandler(null);
    }

    private void configureRememberMeDemo(HttpSecurity http) throws Exception {
        http.rememberMe()
                // 设置记住我参数名称，more是 remember-me
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository())
                // 失效时间，默认两周
                .tokenValiditySeconds(3600 * 24 * 7);
    }

    private void configureAuthorizeRequestsDemo(HttpSecurity http) throws Exception {
        // 授权认证
        http.authorizeRequests()
                // 配置匿名访问接口
                // ant风格匹配器: ?匹配一个字符, *匹配零个或多个字符, **匹配零个或多个目录
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/doLogin").access("permitAll()")
                // hasAuthority, hasAnyAuthority, hasRole, hasAnyRole 严格区分大小写
                .antMatchers("/test").access("hasRole('user')")
                .antMatchers("").hasAuthority("")
                .antMatchers("").hasAnyAuthority("")
                .antMatchers("").hasRole("")
                .antMatchers("").hasAnyRole("")
                .antMatchers("").hasIpAddress("")
                .regexMatchers("").permitAll()
                .mvcMatchers("").servletPath("").permitAll()
                // 其他请求需要授权访问
                .anyRequest().authenticated();
    }

    private void configureFilterDemo(HttpSecurity http) throws Exception {
        http.addFilter(preAuthFilter);
        http.addFilterAt(preAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(preAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(preAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

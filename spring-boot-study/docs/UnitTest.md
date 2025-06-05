# @WebMvcTest的使用场景及配置

主要特点：

- 只加载web层相关的bean，包括:
  - @Controller
  - @ControllerAdvice
  - @JsonComponent
  - WebMvcConfigurer
  - HandlerMethodArgumentResolver
- 不会加载完整的Spring应用上下文
- 自动配置MockMvc
- 默认禁用完整的自动配置，仅应用与MVC测试相关的配置
- 适用于测试特定的Web组件，如拦截器、响应处理器等

常见用法：

```java
// 测试特定Controller
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
  	private ObjectMapper objectMapper;
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;
    
    @Test
    void shouldGetUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(new User("test"));
        
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("test"));
    }
}
```

# @ContextConfiguration的使用场景及配置

主要特点：

- 用于指定如何加载和配置ApplicationContext
- 可以指定配置类或XML配置文件
- 更细粒度地控制测试上下文
- 常用于集成测试场景

常见用法：

```java
// 指定配置类
@ContextConfiguration(classes = {
    TestConfig.class,
    GlobalResponseHandler.class
})
class ServiceTest {
    @Autowired
    private MyService service;
}

// 指定配置文件
@ContextConfiguration(locations = {
    "classpath:applicationContext-test.xml"
})
class XmlConfigTest {
}

// 使用内部配置类
@ContextConfiguration
class ConfigTest {
    @Configuration
    static class TestConfig {
        @Bean
        public MyService myService() {
            return new MyService();
        }
    }
}
```

# @TestPropertySource的使用场景及配置

加载配置属性

```java
@WebMvcTest
@ContextConfiguration(classes = {
    JacksonConfig.class,
    GlobalResponseHandler.class
})
@TestPropertySource(properties = {
    "spring.jackson.date-format=yyyy-MM-dd HH:mm:ss",
    "spring.jackson.time-zone=GMT+8"
})
class ConfigurationTest {
    // 测试代码
}
```

# Spring Boot 各层的单元测试编写建议

主要测试建议：

1. 测试命名规范：
   - 使用描述性的测试方法名
   - 建议使用 "should" 开头，说明期望的行为
   - 包含测试场景和预期结果
2. 测试结构：
   - 遵循 Given-When-Then 模式
   - Given：准备测试数据和条件
   - When：执行被测试的方法
   - Then：验证结果
3. 测试覆盖范围：
   - 正常流程测试
   - 异常流程测试
   - 边界条件测试
   - 参数验证测试
4. 最佳实践：
   - 每个测试方法只测试一个场景
   - 避免测试之间的依赖
   - 使用适当的断言方法
   - Mock 外部依赖
   - 使用合适的测试注解
5. 常用测试工具：
   - JUnit 5：测试框架
   - Mockito：模拟对象
   - AssertJ：流式断言
   - MockMvc：模拟 MVC 请求
   - @DataJpaTest：数据库测试
6. 性能考虑：
   - 使用 @DirtiesContext 谨慎
   - 优先使用 @Transactional 保持测试数据隔离
   - 合理使用测试数据库（如 H2）

## Controller/ControllerAdvice 层测试

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;
    
    @Test
    void shouldGetUser() throws Exception {
        // Given
        User user = new User(1L, "test");
        when(userService.getUser(1L)).thenReturn(user);
        
        // When & Then
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));
    }
}
```

## Service 层测试

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Test
    void shouldCreateUser() {
        // Given
        User user = new User(null, "test");
        User savedUser = new User(1L, "test");
        when(userRepository.save(user)).thenReturn(savedUser);
        
        // When
        User result = userService.createUser(user);
        
        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("test");
        verify(userRepository).save(user);
    }
}
```






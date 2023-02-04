package cn.maiaimei.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SpringMVCTest {
    public static void main(String[] args) {
        testMethodArgumentResolver();
    }

    /**
     * {@link HandlerMethodArgumentResolver}
     */
    @SneakyThrows
    private static void testMethodArgumentResolver() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringMVCTest.class);
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        // 准备Request
        HttpServletRequest request = mockServlet();
        DemoController demoConteoller = new DemoController();
        // 控制器方法被封装为HandlerMethod
        Method method = null;
        Method[] declaredMethods = DemoController.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("testMethodArgumentResolver")) {
                method = declaredMethod;
                break;
            }
        }
        HandlerMethod handlerMethod = new HandlerMethod(demoConteoller, method);
        // 准备对象绑定和类型转换
        WebDataBinderFactory binderFactory = new DefaultDataBinderFactory(new ConfigurableWebBindingInitializer());
        // 准备ModelAndViewContainer，用来存储Model
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        // 解析每个参数
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolvers(
                new RequestParamMethodArgumentResolver(beanFactory, Boolean.TRUE),
                //new RequestResponseBodyMethodProcessor(null),
                new ExpressionValueMethodArgumentResolver(beanFactory) // 解析${}和#{}
                //new RequestParamMethodArgumentResolver(beanFactory, Boolean.FALSE)
        );
        MethodParameter[] parameters = handlerMethod.getMethodParameters();
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            //RequestParamMethodArgumentResolver resolver = new RequestParamMethodArgumentResolver(null, Boolean.FALSE);
            //Object value = resolver.resolveArgument(parameter, mavContainer, new ServletWebRequest(request), binderFactory);
            Object value = resolverComposite.resolveArgument(parameter, mavContainer, new ServletWebRequest(request), binderFactory);
            printParameter(parameter, value);
        }
    }

    private static void printParameter(MethodParameter parameter, Object value) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        String anno = annotations.length > 0 ? "@" + annotations[0].annotationType().getSimpleName() + " " : "";
        String valueClass = value != null ? " (" + value.getClass().getName() + ")" : "";
        System.out.println("[" + parameter.getParameterIndex() + "] "
                + anno
                + parameter.getParameterType().getSimpleName() + " "
                + parameter.getParameterName() + " -> "
                + value + valueClass);
    }

    @SneakyThrows
    private static HttpServletRequest mockServlet() {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.addParameter("name1", "zhangsan");
        request.addParameter("name2", "wangwu");
        request.addParameter("mingzi3", "lisi");
        request.addPart(new MockPart("file2", "file2".getBytes(StandardCharsets.UTF_8)));
        request.addFile(new MockMultipartFile("file1", "file1".getBytes(StandardCharsets.UTF_8)));
        request.addFile(new MockMultipartFile("files", "file1".getBytes(StandardCharsets.UTF_8)));
        request.addFile(new MockMultipartFile("files", "file2".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123456");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token1", "token1"));
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("token2", "token2");
        request.setSession(mockHttpSession);
        request.setAttribute("token3", "token3");
        User user = User.builder().name("Amy").age(18).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String user1AsString = objectMapper.writeValueAsString(user);
        request.setContent(user1AsString.getBytes(StandardCharsets.UTF_8));
        request.setParameter("age", user.getAge().toString());
        request.setParameter("name", user.getName());
        return request;
    }

    private static class DemoController {
        public void testMethodArgumentResolver(
                String name1,
                @RequestParam String name,
                @RequestParam int age,
                @RequestParam String name2,
                @RequestParam(name = "mingzi3") String name3,
                @RequestParam(defaultValue = "${JAVA_HOME}") String javaHome1,
                @RequestParam(name = "file1") MultipartFile file1,
                @RequestParam(name = "files") MultipartFile[] files,
                @PathVariable String id,
                @RequestHeader(name = "Content-Type") String contentType,
                @RequestBody User user1,
                @ModelAttribute(name = "user2") User user2,
                User user3,
                @CookieValue String token1,
                @SessionAttribute String token2,
                @RequestAttribute String token3,
                @RequestPart MultipartFile file2,
                @Value("${JAVA_HOME}") String javaHome2,
                HttpServletRequest httpServlet
        ) {

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class User {
        private String name;
        private Integer age;
    }
}

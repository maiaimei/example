package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.*;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 参数解析核心流程
 * {@link DispatcherServlet#doDispatch(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * {@link RequestMappingHandlerAdapter#handleInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)}
 * {@link ServletInvocableHandlerMethod#invokeAndHandle(org.springframework.web.context.request.ServletWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 * {@link InvocableHandlerMethod#invokeForRequest(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 * {@link InvocableHandlerMethod#getMethodArgumentValues(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 * {@link HandlerMethodArgumentResolverComposite#getArgumentResolver(org.springframework.core.MethodParameter)}
 * {@link HandlerMethodArgumentResolverComposite#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)}
 * <p>
 * 将 BindingAwareModelMap 数据绑定到 request 域中
 * {@link DispatcherServlet#processDispatchResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.HandlerExecutionChain, org.springframework.web.servlet.ModelAndView, java.lang.Exception)}
 * {@link DispatcherServlet#render(org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * {@link AbstractView#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * {@link InternalResourceView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * {@link AbstractView#exposeModelAsRequestAttributes(java.util.Map, javax.servlet.http.HttpServletRequest)}
 */
@Api(tags = "Spring参数解析器")
@Slf4j
@Controller
@RequestMapping("/ArgumentResolver")
public class ArgumentResolverController {
    /**
     * @param port {@link ExpressionValueMethodArgumentResolver}
     */
    @GetMapping("/Value")
    @ResponseBody
    public void test(@Value("${server.port}") String port) {
        log.info("port={}", port);
    }

    @GetMapping("/PathVariable/{id}")
    @ResponseBody
    public void testPathVariable(@PathVariable Integer id) {
        log.info("id={}", id);
    }

    @GetMapping("/PathVariableMap/{k1}/{k2}")
    @ResponseBody
    public void testPathVariableMap(@PathVariable String k1,
                                    @PathVariable String k2,
                                    @PathVariable(required = false) Map map) {
        log.info("k1={}, k2={}, map={}", k1, k2, map);
    }

    @GetMapping("/RequestParam")
    @ResponseBody
    public void testRequestParam(@RequestParam Integer id) {
        log.info("id={}", id);
    }

    /**
     * @param k1   {@link RequestParamMethodArgumentResolver}
     * @param k2   {@link RequestParamMethodArgumentResolver}
     * @param map1 {@link RequestParamMapMethodArgumentResolver}
     * @param map2 {@link MapMethodProcessor}
     */
    @GetMapping("/RequestParamMap")
    @ResponseBody
    public void testRequestParamMap(@RequestParam String k1,
                                    @RequestParam String k2,
                                    @RequestParam(required = false) Map map1,
                                    Map map2) {
        log.info("k1={}, k2={}, map1={}, map2={}", k1, k2, map1, map2);
    }

    /**
     * Content-Type为application/json
     *
     * @param user {@link RequestResponseBodyMethodProcessor}
     */
    @PostMapping("/RequestBody")
    @ResponseBody
    public void testRequestBody(@RequestBody User user) {
        log.info("user={}", user);
    }

    /**
     * Content-Type为application/x-www-form-urlencoded
     *
     * @param user {@link ServletModelAttributeMethodProcessor} and {@link ModelAttributeMethodProcessor}
     */
    @PostMapping("/XWwwFormUrlencoded")
    @ResponseBody
    public void testXWwwFormUrlencoded(User user) {
        log.info("user={}", user);
    }

    @GetMapping("/HttpServletRequest")
    @ResponseBody
    public void testHttpServletRequest(HttpServletRequest request) throws JsonProcessingException {
        log.info("map_k={}", request.getAttribute("map_k"));
        log.info("model_k={}", request.getAttribute("model_k"));
        log.info("request_k={}", request.getAttribute("request_k"));
    }

    /**
     * @param map      {@link MapMethodProcessor}
     * @param model    {@link ModelMethodProcessor}
     * @param request  {@link ServletRequestMethodArgumentResolver}
     * @param response {@link ServletResponseMethodArgumentResolver}
     * @throws ServletException
     * @throws IOException
     */
    @ApiOperation(value = "Map、Model与HttpServletRequest")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", example = "{\"key\":\"value\"}")
    })
    @GetMapping("/testMapModelHttpServletRequest")
    public String testMapModelHttpServletRequest(Map map,
                                                 Model model,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws ServletException, IOException {
        // 相当于 request.setAttribute("map_k", "map_v")
        map.put("map_k", "map_v");
        // 相当于 request.setAttribute("model_k", "model_v")
        model.addAttribute("model_k", "model_v");
        request.setAttribute("request_k", "request_v");

        // 原生Servlet请求转发，Map和Model添加的属性无法绑定到Request域中
        // request.getRequestDispatcher("/ArgumentResolver/HttpServletRequest").forward(request, response);
        // 原生Servlet请求重定向，Map和Model添加的属性无法绑定到Request域中
        // response.sendRedirect("/ArgumentResolver/HttpServletRequest");

        // 请求转发
        return "forward:/ArgumentResolver/HttpServletRequest";
        // 请求重定向，Map和Model添加的属性无法绑定到Request域中
        // return "redirect:/ArgumentResolver/HttpServletRequest";
    }
}

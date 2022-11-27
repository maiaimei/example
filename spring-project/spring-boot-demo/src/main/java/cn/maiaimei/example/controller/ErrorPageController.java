package cn.maiaimei.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPageController {
    /**
     * 无权限访问
     *
     * @return
     */
    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "error/403";
    }

    /**
     * 未找到资源
     *
     * @return
     */
    @RequestMapping("/notfound")
    public String notfound() {
        return "error/404";
    }

    /**
     * 服务器错误
     *
     * @return
     */
    @RequestMapping("/servererror")
    public ModelAndView servererror() {
//        ErrorMap map = ErrorMap.builder()
//                .message("登录账号或密码错误")
//                .build();
//        return new ModelAndView("error/500", map);
        return null;
    }

    /**
     * 服务不可用：限流/熔断/降级
     *
     * @return
     */
    @RequestMapping("/service/unavailable")
    public String serviceUnavailable() {
        return "error/503";
    }
}

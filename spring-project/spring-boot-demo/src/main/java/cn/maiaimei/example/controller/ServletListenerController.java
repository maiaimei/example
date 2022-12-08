package cn.maiaimei.example.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags = "Servlet规范之监听器")
@RestController
@RequestMapping("/servlet/listen")
public class ServletListenerController {
    @GetMapping
    public void listen(HttpServletRequest request,
                       HttpSession session) {
        request.setAttribute("jwt", "jwt");
        request.getAttribute("jwt");
        request.removeAttribute("jwt");

        session.setAttribute("token", "token");
        session.getAttribute("token");
        session.removeAttribute("token");
        session.invalidate();
    }
}

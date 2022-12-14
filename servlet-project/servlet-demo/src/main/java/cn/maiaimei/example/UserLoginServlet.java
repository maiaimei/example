package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/login")
public class UserLoginServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserLoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        log.info("收到用户登录请求");
        log.info("用户：{}", req.getParameter("username"));
        log.info("密码：{}", req.getParameter("password"));

        resp.sendRedirect(req.getContextPath() + "/home.jsp");
    }
}

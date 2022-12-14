package cn.maiaimei.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "helloHttpServlet", value = "/hello-http-servlet")
public class HelloHttpServlet extends HttpServlet {
    private String message;

    @Override
    public void init() {
        message = "Hello HttpServlet!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<h2><a href=\"javascript:history.back();\">back</a></h2>");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
    }
}

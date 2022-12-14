package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloGenericServlet extends GenericServlet {
    private static final Logger log = LoggerFactory.getLogger(HelloGenericServlet.class);

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        log.info("HelloGenericServlet.service");

        servletResponse.setContentType("text/html");

        // Hello
        PrintWriter out = servletResponse.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello, GenericServlet!</h1>");
        out.println("<h2><a href=\"javascript:history.back();\">back</a></h2>");
        out.println("</body></html>");
    }
}

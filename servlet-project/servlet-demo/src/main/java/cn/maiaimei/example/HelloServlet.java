package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet implements Servlet {
    private static final Logger log = LoggerFactory.getLogger(HttpServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        log.info("HelloServlet.init");
    }

    @Override
    public ServletConfig getServletConfig() {
        log.info("HelloServlet.getServletConfig");
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        log.info("HelloServlet.service");

        servletResponse.setContentType("text/html");

        // Hello
        PrintWriter out = servletResponse.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello, Servlet!</h1>");
        out.println("<h2><a href=\"javascript:history.back();\">back</a></h2>");
        out.println("</body></html>");
    }

    @Override
    public String getServletInfo() {
        log.info("HelloServlet.getServletInfo");
        return null;
    }

    @Override
    public void destroy() {
        log.info("HelloServlet.destroy");
    }
}

package cn.maiaimei.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyHttpServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MyHttpServlet.class);

    @Override
    public void init() throws ServletException {
        log.info("MyHttpServlet.init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>MyHttpServlet</h1>");
        out.println("</body></html>");
    }
}

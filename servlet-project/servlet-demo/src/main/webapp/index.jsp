<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Servlet Demo" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet【通过web.xml配置】</a>
<br/>
<a href="hello-generic-servlet">Hello GenericServlet【通过web.xml配置】</a>
<br/>
<a href="hello-http-servlet">Hello HttpServlet【通过@WebServlet注解配置】</a>
<br/>
<p><a href="<%=request.getContextPath()%>/login.jsp">用户登录</a></p>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/user/login" method="post">
    <p>用户：<input type="text" name="username"/></p>
    <p>密码：<input type="password" name="password"/></p>
    <p><input type="submit" value="登录"/></p>
</form>
</body>
</html>

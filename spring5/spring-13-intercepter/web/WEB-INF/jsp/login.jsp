<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 2021/3/13
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<%--web-inf下的所有页面或者资源只能通过controller或这servlet进行访问--%>
<body>
<h1>登录页面</h1>
<form action="${pageContext.request.contextPath}/user/login" method="post">
    用户名：<input type="text" name="username">
    密码：<input type="password" name="password">
    <input type="submit" value="提交"/>
</form>
</body>
</html>

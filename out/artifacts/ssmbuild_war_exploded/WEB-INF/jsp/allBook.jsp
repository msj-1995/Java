<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 2021/3/11
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍列表</title>
    <!--bootStrap美化界面:导入在线cdn-->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!--row clearfix:清除浮动-->
    <div class="row clearfix">
        <!--col-md-12:栅格模式-->
        <div class="col-md-12 column">
            <!--page-header:头部样式-->
            <div class="page-header">
                <h1>
                    <small>书籍列表----------显示所有数据</small>
                </h1>
            </div>
        </div>
        <!--增加书籍按钮-->
        <div class="row">
            <div class="col-md-4 column">
                <%--使用a标签跳转--%>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/toAddBook">新增书籍</a>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/allBook">显示全部书籍</a>
            </div>
            <%--添加下面的div是为了使查询书籍那个div移动到最右边-->
            <%--查询书籍--%>
            <div class="col-md-8 column">
                <form class="form-inline" action="${pageContext.request.contextPath}/book/queryBook" method="post" style="float:right">
                    <span style="color:red;font-weight:bold">${error}</span>
                    <input type="text" name="queryBookName" class="form-control" placeholder="请输入要查询的书籍名称"/>
                    <input type="submit" value="查询" class="btn btn-primary">
                </form>
            </div>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover table-striped">
                <!--thead:表的头部-->
                <thead>
                    <tr>
                        <th>书籍编号</th>
                        <th>书籍名称</th>
                        <th>书籍数量</th>
                        <th>书籍详情</th>
                        <!--增加操作列-->
                        <th>操作</th>
                    </tr>
                </thead>

                <!--tbody:表的主体
                注意：列的写法：头部使用th
                主体使用td
                -->
                <%--书籍从数据库中查询出来，从list中遍历出来--%>
                <tbody>
                    <c:forEach var="book" items="${bookList}">
                        <tr>
                            <td>${book.bookID}</td>
                            <td>${book.bookName}</td>
                            <td>${book.bookCounts}</td>
                            <td>${book.detail}</td>
                            <!--对应操作列-->
                            <td>
                                <a href="${pageContext.request.contextPath}/book/toUpdate?id=${book.bookID}">修改</a>
                                &nbsp;|&nbsp;
                                <!--restful风格传递参数-->
                                <a href="${pageContext.request.contextPath}/book/deleteBook/${book.bookID}">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>

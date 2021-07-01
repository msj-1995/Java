<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改书籍页面</title>
    <!--bootStrap美化界面:导入在线cdn-->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <h1>
                <small>修改书籍</small>
            </h1>
        </div>
    </div>
    <!--form 表单-->
    <form action="${pageContext.request.contextPath}/book/updateBook" method="post">
        <%--出现的问题：我们提交了修改SQL请求，但是修改失败，初次考虑，是事务问题，配置完毕事务，依旧失败！
        看一下SQL语句，能否执行成功：SQL请求失败，修改未完成
        原因是是为传递bookID--%>
        <%--前端传递隐藏域参数--%>
        <input type="hidden" name="bookID" value="${book.bookID}"/>
        <div class="form-group">
            <!--for的作用:点击书籍名称跳转到id(即输入框）
            即点击for后面额的bookName鼠标跳到id为bookName的input-->
            <label for="bookName">书籍名称：</label>
            <!--input中加入required，则该input必须填入值才可以提交
            在input中可以使用value=取到后端传来的参数（取参数使用ESTL表达式）-->
            <input type="text" name="bookName" class="form-control" value="${book.bookName}" id="bookName" required>
        </div>
        <div class="form-group">
            <label for="bookCounts">书籍数量：</label>
            <input type="text" name="bookCounts" class="form-control" value="${book.bookCounts}" id="bookCounts" required>
        </div>
        <div class="form-group">
            <label for="bookDetail">书籍描述：</label>
            <input type="text" name="detail" class="form-control" value="${book.detail}" id="bookDetail" required>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="修改">
        </div>
    </form>
</div>
</body>
</html>

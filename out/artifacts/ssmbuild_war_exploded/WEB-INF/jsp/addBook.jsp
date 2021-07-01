<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!--bootStrap美化界面:导入在线cdn-->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <h1>
                <small>新增书籍</small>
            </h1>
        </div>
    </div>
    <!--form 表单-->
    <form action="${pageContext.request.contextPath}/book/addBook" method="post">
        <div class="form-group">
            <!--for的作用:点击书籍名称跳转到id(即输入框）
            即点击for后面额的bookName鼠标跳到id为bookName的input-->
            <label for="bookName">书籍名称：</label>
            <!--input中的那么属性要与实体类中的name属性一致，否则会报空指针异常-->
            <!--input中加入required，则该input必须填入值才可以提交-->
            <input type="text" name="bookName" class="form-control" id="bookName" required>
            <p class="help-block">例如Mysql</p>
        </div>
        <div class="form-group">
            <label for="bookCounts">书籍数量：</label>
            <input type="text" name="bookCounts" class="form-control" id="bookCounts" required>
            <p class="help-block">例如10</p>
        </div>
        <div class="form-group">
            <label for="bookDetail">书籍描述：</label>
            <input type="text" name="detail" class="form-control" id="bookDetail" required>
            <p class="help-block">例如从删库到跑路</p>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="添加">
        </div>
    </form>
</div>
</body>
</html>

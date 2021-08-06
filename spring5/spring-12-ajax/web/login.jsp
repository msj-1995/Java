<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>登录页面</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.5.1.js"></script>
    <script>
        function uName(){
            $.post({
                url:"${pageContext.request.contextPath}/a3",
                data:{"name":$("#username").val()},
                success:function(data){
                    console.log(data);
                    if(data.toString()==="ok"){
                        //在id为userInfo的标签中添加提示信息
                        $("#userInfo").css("color","green");
                    }else{
                        //用户名输入错误，颜色为红色
                        $("#userInfo").css("color","red");
                    }
                    $("#userInfo").html(data);
                }
            });
        }

        function pwd(){
            $.post({
                url:"${pageContext.request.contextPath}/a3",
                data:{"pwd":$("#password").val()},
                success:function(data){
                    console.log(data);
                    if(data.toString()==="ok"){
                        $("#pwdInfo").css("color","green");
                    }else{
                        $("#pwdInfo").css("color","red");
                    }
                    $("#pwdInfo").html(data);
                }
            });
        }
    </script>
</head>
<body>
<p>
    用户名：<input type="text" id="username" onblur="uName()">
    <!--提示信息-->
    <span id="userInfo"></span>
</p>
<p>
    密码：<input type="password" id="password" onblur="pwd()">
    <span id="pwdInfo"></span>
</p>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.5.1.js"></script>
    <script>
      //$.等价于jQuery.
      function a(){
        $.post({
          //需要一个请求，这个请求在AjaxController类中写
          url:"${pageContext.request.contextPath}/a1",
          //通过data传数据 键值对 其中name是写到后端的参数，即后端需要使用name接收参数
          // #idName,id选择器
          data:{"name":$("#username").val()},
          //回调函数
          success:function(data,status){
            //后端返回了true或false
            alert(data);
            console.log("data=>" + data);
            //status:状态 200 300+，400+....
            console.log("status=>" + status);
          }
        });
      }
    </script>
  </head>
  <body>
  <!--失去焦点的时候，发起一个请求(携带信息）到后台，onblur后是失去焦点时会触发的动作-->
  用户名：<input type="text" id="username" onblur="a()">
  </body>
</html>

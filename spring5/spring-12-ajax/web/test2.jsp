<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>测试</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.5.1.js"></script>
    <script>
        //定位到id为btn并添加一个点击事件
        //外层$(function(){}表示一启动就监听)
        $(function(){
            $("#btn").click(function(){
                console.log("111");
                //jquery的写法还可像下面的 post(参数列表) 简写方式
                // $.post(url,param[可以省略],sucess[回调函数，也可省略（但一般都有回调函数，因为要返回数据）],
                $.post("${pageContext.request.contextPath}/a2",function(data){
                    //data是controller处理后返回的参数
                    console.log(data);
                    /*把data解析输入到id为content的tbody中
                    * 使用js操作dom元素，动态的增加节点*/
                    let html = "";
                    for(let i=0;i<data.length;i++){
                        html += "<tr>" +
                            "<td>" + data[i].name + "</td>" +
                            "<td>" + data[i].age + "</td>" +
                            "<td>" + data[i].sex + "</td>" +
                            "</tr>"
                    }
                    //添加节点到tbody下
                    $("#content").html(html);
                });
            });
        });
    </script>
</head>

<body>
<input type="button" value="加载数据" id="btn">
<table>
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>性别</td>
    </tr>
    <tbody id="content">
    <%--数据:从Ajax中取到--%>
    </tbody>
</table>
</body>
</html>
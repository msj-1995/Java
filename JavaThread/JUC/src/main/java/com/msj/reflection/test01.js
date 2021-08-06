
function f(){
    //js使用var定义变量时，在字符串中和可以使用var定义变量
    var x = "var a=3;var b=5;alert(a+b)";
    //执行代码:x本来是一个静态字符串，但调用eval函数后，可以通过里面定义的变量执行出一个值来
    eval(x);
}

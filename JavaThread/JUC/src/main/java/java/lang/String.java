package java.lang;
//与Java中的String同包同名
public class String {
    public String toString(){
        return "hello";
    }

    public static void main(String[] args) {
        String str = new String();

        //不能访问toString方法，报错，说找不到main方法，说明Java没有走个String类
        //错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
        //   public static void main(String[] args)
        //否则 JavaFX 应用程序类必须扩展javafx.application.Application
        str.toString();

        /*
        *双亲委派机制
        * Java虚拟机由当前的类加载器（如应用程序加载器）层层递进往上找
        * APP(找到String类，但仍往上找）-> WXC ->BOOT(最终执行BOOT中的String）
        * 假设把这个类放到扩展类加载器中（EXC），由于ROOT类中有String类，它仍然执行Boot（根加载器）中的String方法
        * 假设Boot中找不到相应的方法，虚拟机又会向下找，boot->exc->app,现在那个类加载器中找到，就加载执行那个包下的类
        *
        * */

    }
}

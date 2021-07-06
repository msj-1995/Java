package com.msj.demo;

import java.util.concurrent.*;

//线程创建方式三：实现Callable接口，Callable<Boolean>可以加一个Boolean的泛型，该类型于重写的call方法的返回值一样。
//1、实现Callable接口
public class TestCallable implements Callable<Boolean> {
    private String url;  //网络图片地址
    private String name;  //保存文件名

    public TestCallable(String url,String name){
        this.url = url;
        this.name = name;
    }

    //2、重写call()方法
    @Override
    public Boolean call(){
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloder(url,name);
        System.out.println("下载的文件名为：" + name);
        return true;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TestCallable t1 = new TestCallable("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F984%2FceU7xYD3umwA.jpg&refer=http%3A%2F%2F2c.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620301574&t=0539e0e43c26e5e3f4a180a686e1765e","1.jpg");
        TestCallable t2 = new TestCallable("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F984%2FceU7xYD3umwA.jpg&refer=http%3A%2F%2F2c.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620301574&t=0539e0e43c26e5e3f4a180a686e1765e","2.jpg");
        TestCallable t3 = new TestCallable("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F984%2FceU7xYD3umwA.jpg&refer=http%3A%2F%2F2c.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620301574&t=0539e0e43c26e5e3f4a180a686e1765e","3.jpg");

        //3、4、5、6、7步
        //创建执行服务:创建3个执行服务
        ExecutorService ser = Executors.newFixedThreadPool(3);

        //提交执行
        Future<Boolean> r1 = ser.submit(t1);
        Future<Boolean> r2 = ser.submit(t2);
        Future<Boolean> r3 = ser.submit(t3);

        //获取结果:就是call方法返回的结果
        Boolean rs1 = r1.get();
        Boolean rs2 = r2.get();
        Boolean rs3 = r3.get();

        //关闭服务
        ser.shutdown();
    }
}

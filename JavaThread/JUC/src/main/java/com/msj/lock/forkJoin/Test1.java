package com.msj.lock.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class Test1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //test1();  //时间：5306
        //test2();  //时间：3921
        test3();    //时间：115
    }

    //普通程序员
    public static void test1(){
        Long sum = 0L;
        long start = System.currentTimeMillis();
        for(Long i=1L;i<=10_0000_0000;i++){
            sum += i;
        }
        long end = System.currentTimeMillis();
        System.out.println("sum=" + sum + " 时间：" + (end-start));
    }

    //会使用forkJoin的程序员
    public static void test2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        //forkJoin池
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //计算任务
        ForkJoinTask<Long> task = new ForkJoinDemo(0L, 10_0000_0000L);
        //通过池子执行 forkJoinPool.execute():同步执行任务（没有返回值）  forkJoinPool.submit():异步提交（有返回值）
        ForkJoinTask<Long> submit = forkJoinPool.submit(task);
        //获取执行结果
        Long sum = submit.get();
        long end = System.currentTimeMillis();
        System.out.println("sum=" + sum + " 时间：" + (end-start));
    }

    //Stream并行流
    public static void test3(){
        long start = System.currentTimeMillis();
        //Stream并行流 LongStream.rang()范围：()头尾都不包含 LongStream.rangeClosed: (]
        //parallel() 表示并行计算 reduce:取值,Long::sum表示计算求和，类型为Long
        long sum = LongStream.rangeClosed(0L,10_0000_0000L).parallel().reduce(0,Long::sum);
        long end = System.currentTimeMillis();
        System.out.println("sum=" + sum + " 时间：" + (end-start));
    }
}

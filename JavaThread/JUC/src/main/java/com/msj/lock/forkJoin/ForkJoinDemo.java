package com.msj.lock.forkJoin;

import java.util.concurrent.RecursiveTask;

/*
* 求和计算的任务
* 1、普通计算：for循环
* 2、中级： forkJoin
* 3、高级：Stream并行流计算
*
* 如何使用ForkJoin
* 1.forkJoinPool 通过它来执行
* 2.计算任务 forkJoinPool.executor(ForkJoinTask task)
* 3.计算类要继承ForkJoinTask (RecursiveTask)
* */
public class ForkJoinDemo extends RecursiveTask<Long> {
    private Long start;
    private Long end;
    //临界值：到达某个量时就使用forkJoin
    private Long temp = 10000L;

    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    public ForkJoinDemo() {
    }

    //计算方法 返回值就是RecursiveTask<Long>泛型对应的类型
    @Override
    protected Long compute() {
        //未超过临界值，按常规方法计算
        if((end-start)<temp){
            Long sum = 0L;
            //正常计算
            for(Long i =start;i<=end;i++){
                sum+=i;
            }
            return sum;
        }else{
            //分支合并计算 middle是开始和结束的中间值
            long middle = (start+end)/2;
            //计算任务一：里面是递归执行，还可以在细分任务
           ForkJoinDemo task1 = new ForkJoinDemo(start,middle);
           task1.fork();//拆分任务，把任务压入线程队列
           //计算任务队列二
           ForkJoinDemo task2 = new ForkJoinDemo(middle+1,end);
           task2.fork();

           //获取结果 使用join（）
           return task1.join()+task2.join();

        }
    }
}

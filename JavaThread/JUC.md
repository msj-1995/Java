# 1、JUC

该部分的程序代码在普通maven环境下写



什么是juc:java util concurrent

![image-20210405205206295](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210405205206295.png)

学习方式：jdk官方文档+源码：面试高频问



业务：普通的线程代码 Thread

Runnable 没有返回值  效率相比Callable低

![image-20210407222227480](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210407222227480.png)



2、线程和进程

线程、进程：

进程：是一个程序，如QQ.exe,music.exe, .jar包，一个进程可以包含多个线程，至少包含一个。

java默认有几个线程？2个，一个是main线程，一个是GC(垃圾回收机制)线程

线程：如开了一个Typora进程，该进程的写字，自动保存是线程负责的

对于Java而言，开启线程的方法：Thread，Runnable，Callable



问题：Java真的可以开启线程吗？答案是开不了的，如下

```java
public synchronized void start() {
    /**
     * This method is not invoked for the main method thread or "system"
     * group threads created/set up by the VM. Any new functionality added
     * to this method in the future may have to also be added to the VM.
     *
     * A zero status value corresponds to state "NEW".
     */
    if (threadStatus != 0)
        throw new IllegalThreadStateException();

    /* Notify the group that this thread is about to be started
     * so that it can be added to the group's list of threads
     * and the group's unstarted count can be decremented. */
    group.add(this);

    boolean started = false;
    try {
        start0();
        started = true;
    } finally {
        try {
            if (!started) {
                group.threadStartFailed(this);
            }
        } catch (Throwable ignore) {
            /* do nothing. If start0 threw a Throwable then
              it will be passed up the call stack */
        }
    }
}

//本地方法，底层c++,java无法直接操作硬件
private native void start0();
```



并行、并发：

并发编程：

并发（多线程操作同一个资源）。

- cup一核，模拟出来多条线程，多个线程快速交替。

并行（多个人一起行走）

- cup多核，多个线程可以同时执行 线程池

```java
package com.msj.demo01;

public class Test1 {
    public static void main(String[] args) {
        //获取cpu的核数
        //cpu 密集型 IO密集型
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
```



并发编程的本质：充分利用cup的资源



线程的几个状态：六个（点进Thread.State看）

```java
public enum State {
    /**
     * Thread state for a thread which has not yet started.
     */
    //新生
    NEW,

    /**
     * Thread state for a runnable thread.  A thread in the runnable
     * state is executing in the Java virtual machine but it may
     * be waiting for other resources from the operating system
     * such as processor.
     */
    //运行
    RUNNABLE,

    /**
     * Thread state for a thread blocked waiting for a monitor lock.
     * A thread in the blocked state is waiting for a monitor lock
     * to enter a synchronized block/method or
     * reenter a synchronized block/method after calling
     * {@link Object#wait() Object.wait}.
     */
    //锁：阻塞
    BLOCKED,

    /**
     * Thread state for a waiting thread.
     * A thread is in the waiting state due to calling one of the
     * following methods:
     * <ul>
     *   <li>{@link Object#wait() Object.wait} with no timeout</li>
     *   <li>{@link #join() Thread.join} with no timeout</li>
     *   <li>{@link LockSupport#park() LockSupport.park}</li>
     * </ul>
     *
     * <p>A thread in the waiting state is waiting for another thread to
     * perform a particular action.
     *
     * For example, a thread that has called <tt>Object.wait()</tt>
     * on an object is waiting for another thread to call
     * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
     * that object. A thread that has called <tt>Thread.join()</tt>
     * is waiting for a specified thread to terminate.
     */
    //等待：一直等
    WAITING,

    /**
     * Thread state for a waiting thread with a specified waiting time.
     * A thread is in the timed waiting state due to calling one of
     * the following methods with a specified positive waiting time:
     * <ul>
     *   <li>{@link #sleep Thread.sleep}</li>
     *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
     *   <li>{@link #join(long) Thread.join} with timeout</li>
     *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
     *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
     * </ul>
     */
    //超时等待：超时就不等了
    TIMED_WAITING,

    /**
     * Thread state for a terminated thread.
     * The thread has completed execution.
     */
    //终止
    TERMINATED;
}
```



wait/sleep的区别

1、来自不同的类

wait-->Object

sleep-->Thread

企业中 休眠：TimeUnit.(DAY,SECONDS等).sleep(即使用TiemUnit工具类)

2、关于锁的释放

wait会释放锁，sleep睡觉了，抱着锁睡觉，不会释放！

3、使用的范围不同

wait  必须在同步代码块中使用

sleep 可以在任何地方使用

4、是否需要捕获异常

wait 不需要捕获异常（当然中断异常是要捕获的，因为所有的线程都要捕获中断异常）

sleep:需要捕获一个异常（InterruptedException异常）



### 1.1、Lock锁（重点）

传统：synchronized



现在：不去实现Runnable接口，为了解耦，单纯的OOP编程

线程不安全

```java
package com.msj.demo01;

//基本的卖票列子
/*
* 正真的线程开发（公司中的开发）,降低耦合类
* 线程就是一个单独的资源类，没有任何的附属操作
* 1、属性 方法
* */
public class SaleTicket {
    public static void main(String[] args) {
        //资源类
        Ticket ticket = new Ticket();
        //并发：多线程操作同一个资源类(把资源类丢入线程即可）
        //@FunctionalInterface
        //public interface Runnable函数式接口 jdk1.8 lamdba表达式
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"C").start();
    }
}

//卖票的资源类
class Ticket{
    //属性，方法
    private int number = 50;

    //卖票的方式
    public void sale(){
        if(number>0){
            System.out.println(Thread.currentThread().getName()+ "-->卖出了第 " + number-- + " 张票，剩余" + number +"张票");
        }
    }

}
```

出现抢占资源的情况

![image-20210407232148844](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210407232148844.png)



使用传统的Synchronized解决：在卖票方法上加上同步标识符

```java
package com.msj.demo01;

//基本的卖票列子
/*
* 正真的线程开发（公司中的开发）,降低耦合类
* 线程就是一个单独的资源类，没有任何的附属操作
* 1、属性 方法
* */
public class SaleTicket {
    public static void main(String[] args) {
        //资源类
        Ticket ticket = new Ticket();
        //并发：多线程操作同一个资源类(把资源类丢入线程即可）
        //@FunctionalInterface
        //public interface Runnable函数式接口 jdk1.8 lamdba表达式
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"C").start();
    }
}

//卖票的资源类
class Ticket{
    //属性，方法
    private int number = 50;

    //卖票的方式
    public synchronized void sale(){
        if(number>0){
            System.out.println(Thread.currentThread().getName()+ "-->卖出了第 " + number-- + " 张票，剩余" + number +"张票");
        }
    }

}
```

结果正确

![image-20210407232333047](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210407232333047.png)

Synchronized：本质,就是排队



Lock锁

lock接口

![image-20210407232810049](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210407232810049.png)

三个实现类

![image-20210407232759360](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210407232759360.png)

可重入锁的两个构造方法

![image-20210410131822684](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410131822684.png)

公平锁：十分公平，先来后到

非公平锁：十分不公平，可以插队，默认是非公平锁

使用非公平锁的原因，是为了公平（防止耗时长的线程长时间占用资源）

```java
package com.msj.demo02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SaleTicketDemo02 {
    public static void main(String[] args) {
        Ticket2 ticket2 = new Ticket2();
        /*new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"A").start();
        new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"B").start();
        new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"C").start();*/
        //简化
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"A").start();
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"B").start();
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"C").start();
    }
}

//lock锁 三部曲
//1.定义锁new ReentrantLock();  2.加锁 lock.lock() 3.解锁 lock.unlock
class Ticket2{
    private int number = 30;

    //使用可重入锁
    Lock lock = new ReentrantLock();

    public void sale(){
        lock.lock();;
        try{
            //业务代码
            if(number>0){
                System.out.println(Thread.currentThread().getName() + "卖出了第 " + number-- + "票，剩余 " + number + "票");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            //解锁
            lock.unlock();
        }
    }
}
```



Synchronized和Lock的区别：

1.Synchronize是内置的关键字，Lock是一个Java类

2.Synchronized无法判断获取锁的状态，Lock可以判断是否获取到了锁。

3.Synchronized会自动释放锁，lock必须要手动释放锁，（不释放就会造成死锁）

4.Synchronized 线程1（获得锁，阻塞），线程2（等待）；Lock就不一定会一直等下去，

5.Synchronized 可重入锁，不可中断的，非公平；Lock，可重入锁，可以中断锁，非公平（可以自己设置）

6.Synchronized 适合少量的代码同步问题，Lock适合大量的同步代码



锁是什么？如何判断锁的是谁？







生产者和消费者问题

Synchronized  wait notify  （synchronized版）



生产者消费者问题的模板： 判断等待  业务操作  通知

Synchronized版

```java
package com.msj.pc;


/*
* 线程之间的通信问题：生产者消费者问题! 等待唤醒，通知唤醒
* 线程之间交替执行 A B 操作统一个变量，num=0
* A num+1
* B num-1
* */
public class A {
    public static void main(String[] args) {
        Data data = new Data();

        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }
}

//数字 资源类
class Data{
    private int number = 0;

    //+1
    public synchronized void increment() throws InterruptedException {
        if(number!=0){
            //等待
            this.wait();
        }

        number++;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我+1完了(唤醒）
        this.notify();
    }

    //-1
    public synchronized void decrement() throws InterruptedException {
        if(number==0){
            //等待
            wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我-1完毕了（唤醒）
        this.notify();
    }
}
```

 存在问题：现在两个线程是安全的，那么4个、5个线程还安全吗？

```java
package com.msj.pc;


/*
* 线程之间的通信问题：生产者消费者问题! 等待唤醒，通知唤醒
* 线程之间交替执行 A B 操作统一个变量，num=0
* A num+1
* B num-1
* */
public class A {
    public static void main(String[] args) {
        Data data = new Data();

        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }
}

//数字 资源类
class Data{
    private int number = 0;

    //+1
    public synchronized void increment() throws InterruptedException {
        if(number!=0){
            //等待
            this.wait();
        }

        number++;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我+1完了(唤醒）
        this.notify();
    }

    //-1
    public synchronized void decrement() throws InterruptedException {
        if(number==0){
            //等待
            wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我-1完毕了（唤醒）
        this.notify();
    }
}
```

出现问题：2，3等数字都会出现（虚假唤醒）

![image-20210410140642261](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410140642261.png)

虚假唤醒

![image-20210410141127098](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410141127098.png)

![image-20210410141248474](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410141248474.png)

解决虚假判断问题：等待应该在while中去判断等待

```java
//数字 资源类
class Data{
    private int number = 0;

    //+1
    public synchronized void increment() throws InterruptedException {
        //等待
        while(number!=0){
            this.wait();
        }

        number++;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我+1完了(唤醒）
        this.notify();
    }

    //-1
    public synchronized void decrement() throws InterruptedException {
        //等待
        while(number==0){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我-1完毕了（唤醒）
        this.notify();
    }
}
```



面试：单例模式，排序算法，生产者消费者，死锁



JUC版

生产者和消费者问题

通过Lock可以找到Condition类。

![image-20210410141850545](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410141850545.png)



![image-20210410141920890](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410141920890.png)

传统版与JUC版

![image-20210410142027985](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410142027985.png)

代码实现

```java
package com.msj.pc;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 线程之间的通信问题：生产者消费者问题! 等待唤醒，通知唤醒
 * 线程之间交替执行 A B 操作统一个变量，num=0
 * A num+1
 * B num-1
 * */
public class B {
    public static void main(String[] args) {
        Data2 data = new Data2();

        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }
}

//数字 资源类
class Data2{
    private int number = 0;

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    /*
    * condition.await(); 等待
    * condition.signalAll(); 唤醒全部
    * */

    //+1
    public void increment() throws InterruptedException {
        lock.lock();
        try{
            //等待
            while(number!=0){
                condition.await();
            }

            number++;
            System.out.println(Thread.currentThread().getName() + "--> " + number);
            //通知其他线程，我+1完了(唤醒）
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    //-1
    public void decrement() throws InterruptedException {
        lock.lock();
        try{
            //等待
            while(number==0){
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "--> " + number);
            //通知其他线程，我-1完毕了（唤醒）
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
```

输出：线程的唤醒是随机的（Synchronized的唤醒也是随机的）

![image-20210410143154505](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410143154505.png)

任何一个新的技术，绝对不是仅仅只是覆盖了原来的技术，优势和劣势。



Condition：精准的通知和唤醒线程（可以按自己希望的顺序唤醒线程）

代码测试

```java
package com.msj.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
*
* */
public class C{
    public static void main(String[] args) {
        Data3 data = new Data3();
        //目的：希望A执行完通知B执行，B执行完通知C执行，C执行完在通知A执行，如此反复
        new Thread(()->{
            for(int i=0;i<10;i++){
                data.printA();
            }
        },"A").start();
        new Thread(()->{for(int i=0;i<10;i++) data.printB();},"B").start();
        new Thread(()->{for(int i=0;i<10;i++) data.printC();},"C").start();
    }
}


class Data3{
    private Lock lock = new ReentrantLock();
    //Condition 同步监视器，甚至可以一个监视器监视一个线程 condition1 A;condition2:B condition3监视C
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    private int num = 1; //num=1,A执行，2：B执行 3：C执行

    public void printA(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=1){
                //等待
                condition1.await();
            }
            //业务
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒 唤醒指定的人 B
            num = 2;
            //唤醒condition2
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=2){
                //等待
                condition2.await();
            }
            //业务
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒 唤醒指定的人 B
            num = 3;
            //唤醒condition3
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void printC(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=3){
                condition3.await();
            }
            num=1;
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒condition1锁
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    
}
```



### 1.2、八锁现象

如何判断锁的是谁？

**深刻理解我们的锁**

八锁：就是关于锁的八个问题

1、标准情况

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
* 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
* 
* */
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        //两个线程，一个线程打电话，一个线程发消息
        new Thread(()->{
            phone.sendSms();
        },"A").start();
        //发完消息后休息1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}


//资源类
class Phone{

    public synchronized void sendSms(){
        System.out.println("发送消息");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }
}
```

输出：先打印发送消息，后打印打电话

![image-20210410150816411](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410150816411.png)

2、发送消息延时4秒，是先打印那个？

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
* 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
*2、发短信延迟四秒，先打印那个？ 还是先发消息，后打电话
* */
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        //两个线程，一个线程打电话，一个线程发消息
        new Thread(()->{
            phone.sendSms();
        },"A").start();
        //发完消息后休息1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}


//资源类
class Phone{

    public synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送消息");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }
}
```

output

![image-20210410151049255](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410151049255.png)

造成上面两个情况的原因：锁的存在（synchronized（并不是谁先调用谁后调用的问题）

被synchronized修饰的方法（方法块），锁的对象是方法的调用者！由于上面的代码中，资源中的synchronized方法块都是由同一个对象（phone）调用，所以谁先拿到这把锁就是谁先执行。



3、增加了一个普通方法后，线程B不在调用synchronized方法块，而是调用普通方法，先打印谁？

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
 * 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
 *2、发短信延迟四秒，先打印那个？ 还是先发消息，后打电话
 * 3、一个线程不在调用synchronized是是先打印谁？
 * */
public class Test2 {
    public static void main(String[] args) {
        Phone2 phone = new Phone2();

        //两个线程，一个线程打电话，一个线程发消息
        new Thread(()->{
            phone.sendSms();
        },"A").start();
        //发完消息后休息1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.hello();
        },"B").start();
    }
}


//资源类
class Phone2{

    public synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送消息");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }

    public void hello(){
        System.out.println("hello");
    }
}
```

output:1s后打印hello,4s后打印发消息

![image-20210410152046940](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410152046940.png)

原因：普通方法没有同步锁，不是同步方法，不受锁的影响（看执行时间内，普通方法在主方法中顺序执行，如果内在线程执行完前执行，则先输出普通方法中的内容，否则现线程现在执行完则先打印线程中的内容).



4、两个不同的对象执行同步方法，先打印谁？

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
 * 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
 *2、发短信延迟四秒，先打印那个？ 还是先发消息，后打电话
 * 3、一个线程不在调用synchronized是是先打印谁？
 * 4、两个对象执行不同的同步方法，先打印那个？
 * */
public class Test2 {
    public static void main(String[] args) {
        //两个对象
        Phone2 phone = new Phone2();
        Phone2 phone2 = new Phone2();

        //两个线程，一个线程打电话，一个线程发消息
        new Thread(()->{
            phone.sendSms();
        },"A").start();
        //发完消息后休息1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone2.call();
        },"B").start();
    }
}


//资源类
class Phone2{

    public synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送消息");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }

    public void hello(){
        System.out.println("hello");
    }
}
```

output:1s后打印打电话，4s后打印发消息。

现在是两个对象，谁的延迟高就后执行谁，谁的延迟低就先打印谁？



5、静态的同步方法

```java
package com.msj.lock.lock8;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/*
*5、增加两个静态的同步方法
* */
public class Test3 {

    public static void main(String[] args) {
        Phone3 phone = new Phone3();

        new Thread(()->{
            phone.sendSms();
        },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}

class Phone3{
    public static synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发消息");
    }
    public static synchronized void call(){
        System.out.println("打电话");
    }
}
```

先打印发消息，后打印打电话。

![image-20210410153645891](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410153645891.png)

static:静态方法，类一家在就有了，锁的是Class模板，而内存中一个类只有一个class，所以这里phone这个对象调用方法的时候，就把这个对象锁了，从而锁了class类，同样，对类来说也是谁先拿到这个类的锁谁就先执行。



6、两个对象，对象所指向的类中由两个静态方法，两个不同的对象调用不同的静态synchronized块，先打印谁？

```java
package com.msj.lock.lock8;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/*
*5、增加两个静态的同步方法
* */
public class Test3 {

    public static void main(String[] args) {
        Phone3 phone = new Phone3();
        Phone3 phone3 = new Phone3();

        new Thread(()->{
            phone.sendSms();
        },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone3.call();
        },"B").start();
    }
}

class Phone3{
    public static synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发消息");
    }
    public static synchronized void call(){
        System.out.println("打电话");
    }
}
```

先打印的还是发消息

![image-20210410154251847](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410154251847.png)

因为上面的两个对象指向的类是同一个，而static锁的是类模板，而先拿大这个锁的是发消息这个方法，所以先执行的是发消息。



7、一个对象，一个静态同步方法，一个普通同步方法，先打印谁？

```java
package com.msj.lock.lock8;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/*
 *5、增加两个静态的同步方法
 * */
public class Test4 {

    public static void main(String[] args) {
        Phone4 phone = new Phone4();

        new Thread(()->{
            phone.sendSms();
        },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}

class Phone4{
    //静态同步方法
    public static synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发消息");
    }
    //普通同步方法
    public synchronized void call(){
        System.out.println("打电话");
    }
}
```

output:1秒后售出打电话，3秒后输出发消息

![image-20210410154901902](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410154901902.png)

这个虽然是同一个对象，但静态同步块锁的是类模板，普通同步方法锁的是调用者（即phone对象），所以许需要互相等待。



8、静态同步方法和普通同步方法都有的两个对象，两个对象调用不同的方法，先打印谁？

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
 *5、增加两个静态的同步方法
 * */
public class Test4 {

    public static void main(String[] args) {
        Phone4 phone = new Phone4();
        Phone4 phone4 = new Phone4();

        new Thread(()->{
            phone.sendSms();
        },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone4.call();
        },"B").start();
    }
}

class Phone4{
    //静态同步方法
    public static synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发消息");
    }
    //普通同步方法
    public synchronized void call(){
        System.out.println("打电话");
    }
}
```

output:1秒后售出打电话，3秒后输出发消息

![image-20210410155349630](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410155349630.png)

小结：

new出来的对象  this（对象本身） 具体的一个对象（如手机）

static  Classs（模板） 所以个类



### 1.3、集合类不安全

单线程中集合是安全的，但并发开发中就不安全了。

1、List不安全

单线程下安全

```java
package com.msj.lock.unsafe;

import java.util.Arrays;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3");
        list.forEach(System.out::println);
    }
}
```

![image-20210410170132909](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410170132909.png)



并发条件下

```java
package com.msj.lock.unsafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```

报错

java.util.ConcurrentModificationException：并发修改异常，并发下List是不安全的。

![image-20210410170625598](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410170625598.png)

解决方案：

方案一：Vector实现了List接口，并且Vector是安全的，所以使用Vector是安全的,Vectorshijdk1.0出来的，里面使用了sychronized方法块，而List是jdk1.2出来的，而List没有使用synchronized，这是为什么？（基于这个原因，在回答面试官的时候就不能使用Vector来解释）

```java
package com.msj.lock.unsafe;

import java.util.*;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = new Vector<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```

![image-20210410170957965](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410170957965.png)



解决方案二：使用Collections工具包

```java
package com.msj.lock.unsafe;

import java.util.*;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for(int i=0;i<10;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```

 

解决方案三：JUC的解决方案（重点），使用CopyOnWriteArrayList<>(复制并写入类)

![image-20210410171719772](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410171719772.png)

可以进入CopyOnWriteArrayList查看源码

CopyOnWrite 写入时复制，计算机程序设计领域的一种优化策略；

多个线程调用的时候，List读取的时候时固定的，写入时会覆盖，因此就在要写入的时候复制一份（防止多个线程同时写带来的问题），写完后在给调用者，最后调用者在写回去。即防止写入的时候避免复制，从而避免数据不安全的问题。

CopyOnWrite比Vector好的地方：Vector使用的时Synchronized，效率不高，而CopyOnWrite使用的是Lock锁，效率更高。CopyOnWrite的add方法如下：

![image-20210410172545722](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410172545722.png)

 

测试代码：

```java
package com.msj.lock.unsafe;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```



### 1.4、CopyOnWriteArraySet

Set、List之间的关系

![image-20210410173301294](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410173301294.png)



普通线程测试：并发修改异常：

java.util.ConcurrentModificationException

```java
package com.msj.lock.unsafe;

import java.util.HashSet;
import java.util.UUID;

public class TestSet {
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() +"--->" + set);
            },String.valueOf(i)).start();
        }
    }
}
```



使用Collections工具类解决

```java
package com.msj.lock.unsafe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestSet {
    public static void main(String[] args) {
        //HashSet<String> set = new HashSet<>();
        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        for(int i=0;i<10;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() +"--->" + set);
            },String.valueOf(i)).start();
        }
    }
}
```

![image-20210410174222917](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410174222917.png)



JUC的写法

```java
package com.msj.lock.unsafe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class TestSet {
    public static void main(String[] args) {
        //Set<String> set = new HashSet<>();
        //Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() +"--->" + set);
            },String.valueOf(i)).start();
        }
    }
}
```

![image-20210410174511901](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410174511901.png)



使用CopyOnWrite的原因：保证安全和效率





hashSet的底层是什么：hashSet的底层就是hashMap;

```java
public HashSet() {
    map = new HashMap<>();
}
//add set 本质就是map的key,key是无法重复的。
public boolean add(E e) {
        return map.put(e, PRESENT)==null;
}
// PRESENT 是一个常量，不变的值
```



### 1.5、Map集合并发

回顾Map的基本操作

![image-20210410191709704](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410191709704.png)



Map集合也是不安全的

常规：不安全，并发修改异常

```java
package com.msj.lock.unsafe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapTest {
    public static void main(String[] args) {
        //Map是这么用的吗？不是，工作中使用HashMap
        // 默认等价于什么？默认加载因子 0.75，默认等价new HashMap<>(16,0.75);
        Map<String,String> map = new HashMap<>();
        //加载因子，初始化容量（自己去看）

        for(int i=0;i<=30;i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() + "-->" + map);
            },String.valueOf(i)).start();
        }
    }
}
```



使用Collections工具类解决

```java
package com.msj.lock.unsafe;

import java.util.*;

public class MapTest {
    public static void main(String[] args) {
        //Map是这么用的吗？不是，工作中使用HashMap
        // 默认等价于什么？默认加载因子 0.75，默认等价new HashMap<>(16,0.75);
        //Map<String,String> map = new HashMap<>();
        Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
        //加载因子，初始化容量（自己去看）

        for(int i=0;i<=30;i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() + "-->" + map);
            },String.valueOf(i)).start();
        }
    }
}
```



方式二：CopyOnWrite方式

![image-20210410192448996](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410192448996.png)

```java
package com.msj.lock.unsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    public static void main(String[] args) {
        //Map是这么用的吗？不是，工作中使用HashMap
        // 默认等价于什么？默认加载因子 0.75，默认等价new HashMap<>(16,0.75);
        //Map<String,String> map = new HashMap<>();
        //Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String,String> map = new ConcurrentHashMap<>();
        //加载因子，初始化容量（自己去看）

        for(int i=0;i<=30;i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() + "-->" + map);
            },String.valueOf(i)).start();
        }
    }
}
```

作业：研究ConcurrentHashMap的原理。



### 1.6、Callable（简单）

![image-20210410192921347](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410192921347.png)

1、callable有返回值，可以抛出异常

2、方法不同，使用的call()方法，其他使用的是run()方法

![image-20210410193202132](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410193202132.png)

Callable是一个函数式接口，并且方法的返回值就是泛型的类型

```java
package com.msj.lock.unsafe;

import java.util.concurrent.Callable;

public class CallableTest {
    public static void main(String[] args) {

    }
}


class MyThread implements Callable<String>{
    @Override
    public String call() throws Exception {
        return "123456";
    }
}
```



Thread只能启动Runnable，而现在是Callable，要怎么样才能启动，发现Runnable中由FutureTask实现类，而FutureTask类中还有由一个RunnableFuture接口，并且FutureTask的一个有参构造函数中传入了Callable类型的变量，即FutureTask依赖了Callable，通过分析源码知，new Runnable 等价于new FutureTask(Callable callable)(因为FutureTask是Runnable的实现类)

![image-20210410193725299](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410193725299.png)



![image-20210410194021356](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410194021356.png)



FutureTask依赖了Callable

![image-20210410194228987](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410194228987.png)

关系图

![image-20210410194308043](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410194308043.png)



代码测试

```java
package com.msj.lock.unsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //启动Callable
        MyThread myThread = new MyThread();
        //适配类：FutureTask
        FutureTask futureTask = new FutureTask(myThread);
        //执行
        new Thread(futureTask,"A").start();

        //结果放到了FutureTask中，可以通过get方法取到
        Integer integer = (Integer)futureTask.get(); //获取Callable的返回结果
        System.out.println(integer);
    }
}


class MyThread implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}
```

注意：FutureTask的get()方法可能会产生阻塞，因为要等待结果的返回，如果结果是个耗时的操作，应该把获取结果放到最后，或是使用异步获取结果。



两个线程跑时：

```java 
package com.msj.lock.unsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //启动Callable
        MyThread myThread = new MyThread();
        //适配类：FutureTask
        FutureTask futureTask = new FutureTask(myThread);
        //执行
        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start();

        //结果放到了FutureTask中，可以通过get方法取到
        Integer integer = (Integer)futureTask.get(); //获取Callable的返回结果
        System.out.println(integer);
    }
}


class MyThread implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}
```

输入仍只有打印了一个call

![image-20210410195251867](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410195251867.png)

细节：

1、有缓存

2、结果可能需要等待，会阻塞。





### 1.7、常用辅助类

计数类

![image-20210410195505164](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410195505164.png)

1、CountDownLatch 减法计数器

![image-20210410195543084](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410195543084.png)



```java
package com.msj.lock.add;

import java.util.concurrent.CountDownLatch;

//计数器：CountDown 减法
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //总数是6，有必须要执行的人数的时候，在使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for(int i=0;i<=6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + " 走了");
                //数量-1
                countDownLatch.countDown(); //-1
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//等待计数器归零，然后在向下执行
        System.out.println("关门");
    }
}
```

原理：

countDownLatch.countDown()  数量减一

countDownLatch.await();等待计数器归零，然后在向下执行

每次有线程调用countDown数量减一，假设计数器变为零，await()就会被唤醒，继续执行。



2、加法计数器  CyclicBarrier

![image-20210410200745902](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410200745902.png)



加法计数器

```java
package com.msj.lock.add;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        /*
        * 集齐7颗龙珠召唤神龙
        * */

        //召唤龙珠的线程 参数一：计数值 参数二：Runnable参数，可以使用Lambda简化,如果数字该为比7大的数，由于我们for循环中只开了7个线程，数字永远不能达到8以上，所以程序执行完就会阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("召唤神龙成功");
        });

        for(int i=0;i<7;i++){
            //lambda能拿到操作变量i吗：不能拿到，因为lambda是重新new了一个对象，可以使用中间变量来操作
            final int temp = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "收集了 " + temp + " 颗龙珠");
                try {
                    cyclicBarrier.await();//等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```



3、Semaphore 信号量

![image-20210410202119786](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210410202119786.png)

抢车位：6车---3个停车位，只有走了一辆车，另一辆才能停。

可以用来限流

```java
package com.msj.lock.add;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        //参数：线程数量 限流
        Semaphore semaphore = new Semaphore(3);

        //六辆车去抢3个停车位
        for(int i=1;i<=6;i++){
            new Thread(()->{
                //acquire 得到 开启线程后需要先得到一个停车位
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 抢到了车位");
                    //停一会儿
                    TimeUnit.SECONDS.sleep(2);
                    //离开车位
                    System.out.println(Thread.currentThread().getName() + " 离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    //释放
                    semaphore.release();
                }

                //release() 释放
            },String.valueOf(i)).start();
        }
    }
}
```

原理：

semaphore.acquire(); 获得，如果已经满了，则等待，等待资源的释放。

semaphore.release();释放，会将当前的信号量释放+1，然后唤醒等待线程

作用：多个共享资源互斥的使用，在高并发的限流中也有巨大的作用。



# 2、读写锁

![image-20210411092248624](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411092248624.png)

不加锁的情况下

```java
package com.msj.lock.rw;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        //写入
        for(int i=1;i<=5;i++){
            final int temp = i;
            new Thread(()->{
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }

        //读取
        for(int i=1;i<=5;i++){
            final int temp = 1;
            new Thread(()->{
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}

/*
* 自定义缓存
* */

class MyCache{
    private volatile Map<String,Object> map = new HashMap<>();

    //存，写
    public void put(String key,Object value){
        System.out.println(Thread.currentThread().getName() + " 写入" + key);
        map.put(key,value);
        System.out.println(Thread.currentThread().getName() + "写入完毕" + key);
    }

    //读
    public void get(String key){
        System.out.println(Thread.currentThread().getName() + "读取" + key);
        Object o = map.get(key);
        System.out.println(Thread.currentThread().getName() + "读取成功" + key);
    }
}
```

会有插队的现象

![image-20210411093658291](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411093658291.png)

加上读写锁，更加细粒度的控制，希望读的时候可以多个线程同时读，写的时候只有一个线程写。

```java
package com.msj.lock.rw;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        //写入
        for(int i=1;i<=5;i++){
            final int temp = i;
            new Thread(()->{
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }

        //读取
        for(int i=1;i<=5;i++){
            final int temp = i;
            new Thread(()->{
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}

/*
* 自定义缓存
* */

class MyCache{
    private volatile Map<String,Object> map = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //存，写
    public void put(String key,Object value){
        //写锁
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 写入" + key);
            map.put(key,value);
            System.out.println(Thread.currentThread().getName() + "写入完毕" + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    //读
    public void get(String key){
        //读锁
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "读取" + key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读取成功" + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

写入不会被插入，读取可以被插队

![image-20210411094547995](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411094547995.png)



独占锁：写锁，一次只能被一个线程占用

共享锁：读锁

ReadWriteLock

读-读 可以共存

读-写 不能共存

写-写 不能共存



# 3、阻塞队列

队列：先进先出（FIFO）

阻塞：队列满的时候对写入阻塞，取的时候对空队列阻塞。



阻塞队列

![image-20210411095159525](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411095159525.png)

BlockingQueue

List,Set之间的关系

![image-20210411100028557](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411100028557.png)



什么情况下我们会使用阻塞队列：

多线程，线程池



ArrayDeQue双端队列

![image-20210411100302670](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411100302670.png)

关系

![image-20210411100424213](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411100424213.png)

![image-20210411100457102](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411100457102.png)



队列的使用

添加，使用

四组API

1、抛出异常

2、不会抛出异常

3、阻塞等待

4、超时等待

| 方式       | 抛出异常  | 有返回值 | 阻塞等待 | 超时等待  |
| ---------- | --------- | -------- | -------- | --------- |
| 添加       | add()     | offer()  | put()    | offer(,,) |
| 移除       | remove()  | poll()   | take()   | poll(,,)  |
| 判断队列首 | element() | peek()   | -        | -         |

抛出异常

```java
package com.msj.lock.bq;

import java.util.concurrent.ArrayBlockingQueue;

public class Test1 {
    public static void main(String[] args) {
        test1();
    }

    /*
     * 抛出异常
     * */
    public static void test1(){
        //参数为队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        //添加元素，有返回值（布尔值，添加成功返回true，失败返回false)
        System.out.println(blockingQueue.add("A"));
        System.out.println(blockingQueue.add("B"));
        System.out.println(blockingQueue.add("C"));
        //再添加一个元素:抛出异常：队列已满 Queue full
        //System.out.println(blockingQueue.add("D"));

        //查看队首元素是谁
        System.out.println(blockingQueue.element());
        //弹出元素
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        //元素弹完后再出队列 抛出异常：java.util.NoSuchElementException 没有元素
        System.out.println(blockingQueue.remove());
    }
}
```

有返回值，没有异常

```java
public static void test2(){
    /*
    * 不抛出异常
    * */
    ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);
    System.out.println(blockingQueue.offer("A"));
    System.out.println(blockingQueue.offer("B"));
    System.out.println(blockingQueue.offer("C"));
    //不抛出异常，队列满了再添加会返回false
    System.out.println(blockingQueue.offer("D"));

    //查看队首元素是谁
    System.out.println(blockingQueue.peek());
    //弹出来
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.peek());
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    //对立空继续弹出不跑出异常，而是返回null
    System.out.println(blockingQueue.poll());
}
```

![image-20210411102628269](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411102628269.png)





阻塞等待

```java
package com.msj.lock.bq;

import java.util.concurrent.ArrayBlockingQueue;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    /*
     * 抛出异常
     * */
    public static void test1(){
        //参数为队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        //添加元素，有返回值（布尔值，添加成功返回true，失败返回false)
        System.out.println(blockingQueue.add("A"));
        System.out.println(blockingQueue.add("B"));
        System.out.println(blockingQueue.add("C"));
        //再添加一个元素:抛出异常：队列已满 Queue full
        //System.out.println(blockingQueue.add("D"));

        //弹出元素
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        //元素弹完后再出队列 抛出异常：java.util.NoSuchElementException 没有元素
        System.out.println(blockingQueue.remove());
    }

    public static void test2(){
        /*
        * 不抛出异常
        * */
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B"));
        System.out.println(blockingQueue.offer("C"));
        //不抛出异常，队列满了再添加会返回false
        System.out.println(blockingQueue.offer("D"));

        //查看队首元素是谁
        System.out.println(blockingQueue.peek());
        //弹出来
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //对立空继续弹出不跑出异常，而是返回null
        System.out.println(blockingQueue.poll());
    }

    public static void test3() throws InterruptedException {
        /*
        *等待阻塞：一直阻塞
        * */
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        blockingQueue.put("A");
        blockingQueue.put("B");
        blockingQueue.put("C");
        //队列满，会一直等待
        //blockingQueue.put("D");

        //取
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());  //没有这个元素，也会一直阻塞
    }
}
```



阻塞等待，超时退出

```java
public static void test4() throws InterruptedException {
    /*
    * 阻塞等待，超时退出
    * */
    ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);
    blockingQueue.offer("A");
    blockingQueue.offer("B");
    blockingQueue.offer("C");
    //超时2s后退出
    blockingQueue.offer("D",2, TimeUnit.SECONDS);

    //取
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    //等待2秒拿不到数据就不拿了
    System.out.println(blockingQueue.poll(2,TimeUnit.SECONDS));


}
```

![image-20210411103616564](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411103616564.png)



### 3.1、同步队列

同步队列：SynchronousQeue

他是BlockingQueue下的一个子类

同步队列，没有容量，进去一个元素，必须等待取出来之后，才能往里面再放元素

存取操作：put,take

和其他blockingQueue不一样，同步队列不存储元素，put了一个元素，必须从里面take取出来，否则不能再put进去

```java
package com.msj.lock.bq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueDemo {
    public static void main(String[] args) {
        //SynchronousQueue<String>  blockingQueue = new SynchronousQueue<String>();同步队列，等价于下面的语句
        BlockingQueue<String> blockingQueue = new SynchronousQueue<String>();

        //当前线程put三次
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + "put 1");
                blockingQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "put 2");
                blockingQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "put 3");
                blockingQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"T1").start();

        //取线程
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + "取值" + blockingQueue.take());
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + "取值" + blockingQueue.take());
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + "取值" + blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"T2").start();
    }
}
```



# 4、线程池（重点）

池化技术

程序的运行，本质:占用系统的资源，优化资源的使用===>池化技术

线程池，连接池，对象池.....

池化技术：事先准备好一些资源，有人要用，就可以直接来拿，用后还回去。

线程池的好处：

1、降低资源的消耗

2、提高相应速度

3、方便管理

线程复用，可以控制最大并发数，管理线程。



线程池必会：

线程池：三大方法

使用工具类创建线程的三大方法

1、单例模式，所有的任务都是由一个线程执行

```java
package com.msj.lock.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolDemo01 {
    public static void main(String[] args) {
        /*Executors  工具类 三大方法
        Executors.newSingleThreadExecutor(); //单个线程
        Executors.newFixedThreadPool(5); //创建一个固定大小的线程池
        Executors.newCachedThreadPool();  //可伸缩的线程池，遇强则强，遇弱则弱
*/
        //学了县城后使用线程池来创建线程
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        //执行线程
        try {
            for(int i=0;i<10;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

![image-20210411110636841](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411110636841.png)



2、固定大小：

```java
package com.msj.lock.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolDemo01 {
    public static void main(String[] args) {
        /*Executors  工具类 三大方法
        Executors.newSingleThreadExecutor(); //单个线程
        Executors.newFixedThreadPool(5); //创建一个固定大小的线程池
        Executors.newCachedThreadPool();  //可伸缩的线程池，遇强则强，遇弱则弱
*/
        //学了县城后使用线程池来创建线程:创建5个线程
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //执行线程
        try {
            for(int i=0;i<10;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

最多5个线程执行

![image-20210411110715331](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411110715331.png)

3、可伸缩型：10个任务，最多会有10个线程执行。

```java
package com.msj.lock.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolDemo01 {
    public static void main(String[] args) {
        /*Executors  工具类 三大方法
        Executors.newSingleThreadExecutor(); //单个线程
        Executors.newFixedThreadPool(5); //创建一个固定大小的线程池
        Executors.newCachedThreadPool();  //可伸缩的线程池，遇强则强，遇弱则弱
*/
        //学了县城后使用线程池来创建线程:创建5个线程
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //执行线程
        try {
            for(int i=0;i<10;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

![image-20210411110835468](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411110835468.png)





7大参数

源码分析

```java
//Executors.newSingleThreadExecutor()
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    
}


//Executors.newFixedThreadPool();
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
}


//Executors.newCachedThreadPool();
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,  //约等于21亿
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
}


//本质：都是执行了ThreadPoolExecutor,点进去
public ThreadPoolExecutor(int corePoolSize, //核心线程池大小
                          //最大核心数线程
                              int maximumPoolSize,
                          //超时了没有调用就会释放
                              long keepAliveTime,
                          //超时单位
                              TimeUnit unit,
                          //阻塞队列
                              BlockingQueue<Runnable> workQueue,
                          //线程工厂，创建线程的，一般不用动
                              ThreadFactory threadFactory,
                          //拒绝策略
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.acc = System.getSecurityManager() == null ?
                null :
                AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
}
```



![image-20210411111954606](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411111954606.png)



![image-20210411112248294](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411112248294.png)



手动创建一个线程池

使用原生的ThreadPoolExecutor创建线程池

```java
package com.msj.lock.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        //下面的阻塞队列使用的链表队列：new LinkedBlockingQueue<>(3)
        //线程工厂使用默认的线程工程：Executors工具类中有,拒绝策略也可去工具类中找默认值
        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,3,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

        //执行线程
        try {
            for(int i=0;i<5;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

业务没有超过最大线程池数5，使用的是核心线程处理（2个）

![image-20210411113625525](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411113625525.png)

最大承载=队列（Deque）+ max

6个业务：由于有3个阻塞队列+2个核心线程<6,故触发一个备用备用线程，会有3个线程处理

```java
package com.msj.lock.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        //下面的阻塞队列使用的链表队列：new LinkedBlockingQueue<>(3)
        //线程工厂使用默认的线程工程：Executors工具类中有,拒绝策略也可去工具类中找默认值
        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,3,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

        //执行线程
        try {
            for(int i=0;i<6;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

![image-20210411113935795](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411113935795.png)

同理，7个业务会有4个线程护理，8个及以上会有5个线程处理，因为最大线程数为5.超出最大承载时8个以上（不包含8，因为阻塞队列3+最大线程5=8）会抛出拒绝策略异常:java.util.concurrent.RejectedExecutionException:（可以使用其他拒绝策略防止抛出异常）

```java
package com.msj.lock.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        //下面的阻塞队列使用的链表队列：new LinkedBlockingQueue<>(3)
        //线程工厂使用默认的线程工程：Executors工具类中有,拒绝策略也可去工具类中找默认值
        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,3,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

        //执行线程
        try {
            for(int i=0;i<9;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

![image-20210411114230526](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411114230526.png)



四种拒绝策略

![image-20210411112934852](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411112934852.png)

默认使用的是：

```java
private static final RejectedExecutionHandler defaultHandler =
    new AbortPolicy();  //线程池满了（包括阻塞队列），还有线程进来，不处理这个新类的线程，抛出异常

CallerRunsPolicy();//哪来的去那里，即如果线程池满了，就有创建或使用该线程的线程去处理
DiscardPolicy(); //队列满了，不会抛出异常，但是多出来的任务会被抛弃，不执行
DiscardOldestPolicy(); //队列满了尝试和最早的任务竞争，如果经常失败，则该任务失败，会被抛弃。
```



CallerRunsPolicy()：多出来的线程创建该线程的主线程处理

```java
package com.msj.lock.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        //下面的阻塞队列使用的链表队列：new LinkedBlockingQueue<>(3)
        //线程工厂使用默认的线程工程：Executors工具类中有,拒绝策略也可去工具类中找默认值
        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,3,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());

        //执行线程
        try {
            for(int i=0;i<9;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
```

![image-20210411114856216](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411114856216.png)

最大线程数到底该如何定义？

1、cpu密集型：cup有几核，就定义为多少，可以保持cpu的效率最高。

2、IO密集型  假如有15个大型任务，io十分占用资源，那么设置的最大线程数应该大于15，一般设置为大型任务的两倍。



cpu密集型，获取cpu核数的正确姿态：

```java
System.out.println(Runtime.getRuntime().availableProcessors())
```



# 5、四大函数式接口（必须掌握）

重点加简单

新时代的程序员:lambda表达式、链式编程、函数式编程、Stream流式计算

框架底层有大量的FunctionalInterface

![image-20210411191843005](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411191843005.png)

代码测试

1、函数型接口

```
Function 函数型接口
最常用的就是new一个接口，变成匿名内部类
Function function = new Function(){
  @Override
  public Object apply(Object o){
    return null;
  }
}
```

![image-20210411192030426](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411192030426.png)



![image-20210411192352731](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411192352731.png)

```java
package com.msj.lock.function;

import java.util.function.Function;

/*
* Function 函数型接口
* */
public class Demo01 {
    public static void main(String[] args) {
        Function function = new Function<String,String>(){
            //工具类 输出传入的参数
            @Override
            public String apply(String str) {
                return str;
            }
        };
        System.out.println(function.apply("abcdefg"));
    }
}
```

特点：

函数型接口：有一个输入参数，有一个输出参数，只要是函数型接口，可以使用lambda简化。

刚才的代码使用lambda简化

```java
package com.msj.lock.function;

import java.util.function.Function;

/*
* Function 函数型接口
* */
public class Demo01 {
    public static void main(String[] args) {
        /*Function function = new Function<String,String>(){
            //工具类 输出传入的参数
            @Override
            public String apply(String str) {
                return str;
            }
        };*/
        //使用lambda表达式简化
        //Function function = (str)->{return str;};
        //再简化：一个参数时可以去掉()
        Function function = str->{return str;};
        System.out.println(function.apply("abcdefg"));
    }
}
```



2、断定型接口

Predicate<T>,只有一个输入参数，返回值是Boolean只

![image-20210411193436199](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411193436199.png)

```java
package com.msj.lock.function;

import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        //判断字符串是否为空:为空返回true,否则返回false
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return str.isEmpty();
            }
        };
        System.out.println(predicate.test("123"));

        //lambda简化
        Predicate<String> predicate1 = (str)->{
            return str.isEmpty();
        };
        System.out.println(predicate1.test(""));
    }
}
```

![image-20210411194114417](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411194114417.png)



3、Consumer接口：消费型接口，有一个输入值，没有返回值

![image-20210411194323745](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411194323745.png)

```java
package com.msj.lock.function;

import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("hello,你好呀");

        //lambda简化
        Consumer<String> consumer1 = (str)->{
            System.out.println(str);
        };
        consumer1.accept("hello,hello!");
    }
}
```

![image-20210411194706000](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411194706000.png)





4、Supplier接口：共给型接口,没有参数，只有返回值

![image-20210411194820361](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411194820361.png)



```java
package com.msj.lock.function;

import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 1024;
            }
        };
        System.out.println(supplier.get());

        //lambda简化
        Supplier<Integer> supplier1 = ()->{
            return 1024;
        };
        System.out.println(supplier1.get());
    }
}
```

![image-20210411195103105](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411195103105.png)





# 6、流式计算

Stream：流式计算

什么是流式计算？

大数据：存储+计算

集合、Mysql本质是用来存储东西的；

计算都应该交给流来操作。



作业

![image-20210411195424377](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411195424377.png)

Stream流：

输出id为偶数的用户

```java
package com.msj.lock.stream;

import java.util.Arrays;
import java.util.List;

/*
* 1分钟内完成此题，只能用一行代码实现
* 现有5个用户，筛选：
* 1.ID必须是偶数
* 2.年龄必须大于23岁
* 3.用户名转为大写字母
* 4.用户名字母倒着排序
* 5.只能输出一个用户
* */
public class Test {
    public static void main(String[] args) {
        User u1 = new User(1,"msj",21);
        User u2 = new User(2,"mym",22);
        User u3 = new User(3,"xhm",23);
        User u4 = new User(4,"ly",24);
        User u5 = new User(5,"yyj",25);
        User u6 = new User(6,"yj",26);
        //集合就是存储
        List<User> users = Arrays.asList(u1,u2,u3,u4,u5);
        //计算交给流：把集合转换为流 list.stream()
        //filter：过滤，他要接收一个参数，该参数属于断定型接口的函数式接口 u.getId()%2==0表示id为偶数的用护
        users.stream().filter(u->{return u.getId()%2==0;}).forEach(System.out::println);
    }

}
```

![image-20210411201502697](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411201502697.png)

下面的问题同理

![image-20210411201910208](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411201910208.png)

filter函数中返回的是一个对象，该对象类型由泛型T规定，而泛型T的类型是调用stream（）对象的泛型类型（即User）。

```java
package com.msj.lock.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*
* 1分钟内完成此题，只能用一行代码实现
* 现有5个用户，筛选：
* 1.ID必须是偶数
* 2.年龄必须大于23岁
* 3.用户名转为大写字母
* 4.用户名字母倒着排序
* 5.只能输出一个用户
* */
public class Test {
    public static void main(String[] args) {
        User u1 = new User(1,"msj",21);
        User u2 = new User(2,"mym",22);
        User u3 = new User(3,"xhm",23);
        User u4 = new User(4,"ly",24);
        User u5 = new User(5,"yyj",25);
        User u6 = new User(6,"yj",26);
        //集合就是存储
        List<User> users = Arrays.asList(u1,u2,u3,u4,u5,u6);
        //计算交给流：把集合转换为流 list.stream()
        //filter：过滤，他要接收一个参数，该参数属于断定型接口的函数式接口 u.getId()%2==0表示id为偶数的用护
        users.stream().filter(u->{return u.getId()%2==0;})
                .filter(u->{return u.getAge()>23;})
                //map(u->{return u.getName().toUpperCase();})：传入的形式参数为User类型的u,返回值为String
                .map(u->{return u.getName().toUpperCase();})
                //用户名字母倒着排序 sorted():默认正序
                .sorted((uu1,uu2)->{return uu2.compareTo(uu1);})
                //只输出一个用户
                .limit(1)
                .forEach(System.out::println);
    }

}
```



# 7、ForkJoin

forkJoin:分支合并

什么是ForkJoin：forkJoin在JDK1.7中出来的，提高效率，大数据量。

大数据：Map Reduce

![image-20210411203542282](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411203542282.png)

ForkJoin特点：工作窃取，比如两个线程A、B同时执行，B线程执行的比较快，在A线程先执行完，这时B停下来等待A就会造成资源的浪费，为了提高效率，B会将A未执行完的工作那一部分过来执行，这就是工作窃取。这个里面维护的都是双端队列。

![image-20210411203655579](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411203655579.png)

forkJoin中也有一个执行器：executor,需要传入一个ForkJoinTask对象

![image-20210411205017132](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411205017132.png)

![image-20210411205110263](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411205110263.png)



![image-20210411205131055](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411205131055.png)

递归任务，有返回值，需要自己写一个类去继承它

![image-20210411205232911](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411205232911.png)

继承后需要实现里面没有实现的抽象方法

![image-20210411205317025](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411205317025.png)





ForkJoin的列子：我们的任务是由返回值的

普通计算：效率低

```java
package com.msj.lock.forkJoin;

/*
* 求和计算的任务
* 1、普通计算：for循环
* 2、中级： forkJoin
* 3、高级：Stream并行流计算
* */
public class forkJoinDemo {
    public static void main(String[] args) {
        int sum = 0;
        //1-10亿的和
        for(int i=1;i<=10_0000_0000;i++){
            sum+=i;
        }
        System.out.println(sum);
    }
}
```

ForkJoin:

```java
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
```



Stream流：

![image-20210411212459839](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411212459839.png)





# 8、异步回调

Futur：异步回调，设计的初衷，对将来某个事件的结果进行建模

![image-20210411213440735](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411213440735.png)

Future的实现类

![image-20210411213641407](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411213641407.png)

执行异步任务的方式

![image-20210411214431431](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411214431431.png)

没有返回值的异步回调

```java
package com.msj.lock.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
* 异步调用：可以想象为Ajax
*
* 步骤 异步调用：CompletableFuture
* 1、异步执行
* 2、成功回调
* 3、失败回调
* */
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //发起一个请求 如果没有返回值，泛型写Void(注意类型不能写小写，需要用大写，包装类） CompletableFuture<Void>
        //CompletableFuture.runAsync();使用Runnable方式启动
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "runAsync=>Void");
        });

        //由于是异步执行，在线程休眠的时候应该不影响主线程的执行
        System.out.println("1111111111");
        //获取执行结果：completableFuture.get();会阻塞等待结果
        completableFuture.get();
    }
}
```

![image-20210411215024109](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411215024109.png)



BiConsumer：增强版的Consumer，可以传入两个参数，但没有返回值

![image-20210411215628599](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411215628599.png)





有返回值的异步回调

```java
package com.msj.lock.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //有返回值的异步回调：CompletableFuture.supplyAsync()需要传入一个共给型的函数接口
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() + " supplyAsync-->completableFuture");
            return 1024;
        });

        //completableFuture.whenComplete()编译成功时的回调，传入参数为BiConsumer,增强版的消费型接口
        System.out.println(completableFuture.whenComplete((t, u) -> {
            System.out.println("t-->" + t);
            System.out.println("u-->" + u);
            //exceptionally()编译失败的回调(参数是一个函数型接口） 传的参数的异常类,
        }).exceptionally((e) -> {
            //e.getMessage()获取异常信息
            System.out.println(e.getMessage());
            //打印堆栈信息
            e.printStackTrace();
            return 233;
            //get()：获得结果
        }).get());
        //正常返回1024，失败返回223
    }
}
```

执行成功

![image-20210411220709791](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411220709791.png)



执行失败的异步回调：经典错误：1/0

```java
package com.msj.lock.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //有返回值的异步回调：CompletableFuture.supplyAsync()需要传入一个共给型的函数接口
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() + " supplyAsync-->completableFuture");
            int i=1/0;
            return 1024;
        });

        //completableFuture.whenComplete()编译成功时的回调，传入参数为BiConsumer,增强版的消费型接口
        System.out.println(completableFuture.whenComplete((t, u) -> {
            //t:回调成功时的放回值 u:回调错误时的错误信息
            System.out.println("t-->" + t);
            System.out.println("u-->" + u);
            //exceptionally()编译失败的回调(参数是一个函数型接口） 传的参数的异常类,
        }).exceptionally((e) -> {
            //e.getMessage()获取异常信息
            System.out.println(e.getMessage());
            //打印堆栈信息
            e.printStackTrace();
            return 233;  //可以获取到错误的返回结果
            /*
            success:200
            error:404,500......
            */
            //get()：获得结果
        }).get());
        //正常返回1024，失败返回223
    }
}
```

![image-20210411220821935](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411220821935.png)





# 9、JMM

请你谈谈对Volatile的理解

Volatile是Java虚拟机提供的轻量级的同步机制（和synchronized差不多，不过volatile是轻量级的），作用

1、保证可见性

2、不保证原子性

3、禁止指令重排



如何保证可见性：与jvm挂钩

什么是JMM(java内存模型，不存在的东西，是概念，约定)

关于JMM的一些同步约定：

1、线程解锁前：必须把共享变量立刻刷会主存（因为线程工作的时候是把主存中的东西考到了自己的工作空间中，他是在自己的工作空间中修改值，如果是共享变量，需要在自己修改后立刻刷回主存）

2、线程加锁前，必须读取内存中的最新值到工作内存中

3、加锁和解锁需保证是同一把锁



线程：工作内存 主存

八大操作：

read:读操作（读内存中的数据）

load：加载操作（加载到线程的工作空间中）

use:使用操作（执行引擎（用户等）使用）

assign:返回操做（执行引擎使用完后刷新的到最新值），赋值

write:写入操作（写入缓冲区等）

store:存操作（存到主存）

lock:加锁

unlock:解锁

![image-20210411223615903](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411223615903.png)



![image-20210411223346684](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411223346684.png)



多线程

![image-20210411223425183](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411223425183.png)

存在问题：线程B修改了值，但是线程A不能及时可见，如何解决？

Volatile

```java
package com.msj.lock.volatileTest;

import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private static int num = 0;
    public static void main(String[] args){
        //main线程（主线程）

        //在添加一个线程一
        new Thread(()->{
            while(num==0){

            }
        }).start();;

        //主线程休息1s，是为了保证子线程能够成功启动
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num=1;
        System.out.println(num);
    }
}
```

发现输出了1（主线程输出），但是程序一直没有停止，因为子线程，拿到的num是0，在主线程修改num的值为1后，并没通知子线程num的已经修改，于是子线程一直在不停的循环。这就需要子线程直到主存中的值发生了变化。

![image-20210411224505015](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411224505015.png)

为了使线程直到主存中的值发生了变化，这就引出了Volatile关键字

```java
package com.msj.lock.volatileTest;

import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private volatile static int num = 0;
    public static void main(String[] args){
        //main线程（主线程）

        //在添加一个线程一
        new Thread(()->{
            while(num==0){

            }
        }).start();;

        //主线程休息1s，是为了保证子线程能够成功启动
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num=1;
        System.out.println(num);
    }
}
```

不加volatile程序会陷入死循环，加了Volatile，只要主存中的值一变，线程也会知道。



测试不保证原子性

原子性：不可分割

线程A在执行任务的时候是不允许被打扰的，也不能被分割，要么同时成功要么同时失败。

```Java
package com.msj.lock.volatileTest;

//不保证原子性
public class VolatileDemo02 {
    private static int num = 0;

    public static void add(){
        num++;
    }

    public static void main(String[] args) {
        //理论上：num的值为1000*20=2万
        for(int i=1;i<=20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    add();
                }
            }).start();
        }
        //判断还有多少线程在执行：Java中有两条默认的线程：main和gc线程，如果存活的线程数>2证明还有线程在执行
        while(Thread.activeCount()>2){
            //礼让，如果线程没执行完，我们就让他礼让一下
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " " + num);
    }
}
```

测试结果很难到达两万

![image-20210411225710080](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411225710080.png)



解决方法一：synchronized

```java
package com.msj.lock.volatileTest;

//不保证原子性
public class VolatileDemo02 {
    private static int num = 0;

    public synchronized static void add(){
        num++;
    }

    public static void main(String[] args) {
        //理论上：num的值为1000*20=2万
        for(int i=1;i<=20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    add();
                }
            }).start();
        }
        //判断还有多少线程在执行：Java中有两条默认的线程：main和gc线程，如果存活的线程数>2证明还有线程在执行
        while(Thread.activeCount()>2){
            //礼让，如果线程没执行完，我们就让他礼让一下
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " " + num);
    }
}
```

 查看volatile也不保证原子性

```java
package com.msj.lock.volatileTest;

//不保证原子性
public class VolatileDemo02 {
    private volatile static int num = 0;

    public static void add(){
        num++;
    }

    public static void main(String[] args) {
        //理论上：num的值为1000*20=2万
        for(int i=1;i<=20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    add();
                }
            }).start();
        }
        //判断还有多少线程在执行：Java中有两条默认的线程：main和gc线程，如果存活的线程数>2证明还有线程在执行
        while(Thread.activeCount()>2){
            //礼让，如果线程没执行完，我们就让他礼让一下
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " " + num);
    }
}
```



如果不加lock锁和synchronized，如何保证原子性？

反编译看底层源码：直到num++不是一个原子操作，需要分为多步执行

![image-20210411230133902](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411230133902.png)

JUC中的原子性包

![image-20210411230318526](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411230318526.png)

上面的问题可以使用原子类解决：

```java
package com.msj.lock.volatileTest;

import java.util.concurrent.atomic.AtomicInteger;

//不保证原子性
public class VolatileDemo02 {
    //原子类的Integer
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add(){
        //num加1操作 原子类的加1操作 使用的是CAS，是cpu的并发操作，效率非常的高
        num.getAndIncrement();
    }

    public static void main(String[] args) {
        //理论上：num的值为1000*20=2万
        for(int i=1;i<=20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    add();
                }
            }).start();
        }
        //判断还有多少线程在执行：Java中有两条默认的线程：main和gc线程，如果存活的线程数>2证明还有线程在执行
        while(Thread.activeCount()>2){
            //礼让，如果线程没执行完，我们就让他礼让一下
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " " + num);
    }
}
```

![image-20210411230800758](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411230800758.png)

原子类的底层都直接和操作系统挂钩，在内存中修改值。unsafe类是一个很特殊的存在。



测试禁止指令重排

指令重排：我们写的程序，计算机并不是按照我们写的那样去执行的。

源代码-->编译器优化重排-->指令并行也可能会重排-->内存系统也会重排-->执行

```java
int x = 1;  //1
int y = 2;  //2
x = x + 5;  //3
y = x * x;  //4

//我们希望的是计算机顺序执行：1-2-3-4
//上面的代码可能的执行顺序： 1234 2134 1324
//但不可能是4123，指令重排会考虑数据之间的依赖性
```

指令重排可能造成的影响:abxy这四个值默认都是0

| 线程A | 线程B |
| ----- | ----- |
| x=a   | y=b   |
| b=1   | a=2   |
|       |       |

正常的结果：x=0,y=0

但是由于指令重排，可能

| 线程A     | 线程B |
| --------- | ----- |
| b=1先执行 | a=2   |
| x=a       | y=b   |
|           |       |

对于线程A来说b=1和x=a谁先执行我所谓，B同样如此，但导致的结果就是结果错误：

x=2,y=1

只要加了volatile就可以避免指令重排。

内存屏障，cup指令，作用：

1、保证特定的操作的执行顺序

2、可以保证某些变量的内存可见性（利用这些特性volatile实现了可见性）

![image-20210411232415107](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210411232415107.png)

Volatile是可以保持可见性，不能保证原子性，由于内存屏障，可以保证避免指令重排现象的产生。



# 10、彻底玩转单例模式

饿汉式、DCL懒汉式



饿汉式单例

```java
package com.msj.lock.single;

//饿汉式单例
public class Hungry {
    //饿汉式浪费内存，一加载类就初始化类对象，加入这个对象中由很多占内存的数组，而数组中由没有初始化东西（即空间被占用而未被使用），就会造成内存的浪费
    private byte[] data1 = new byte[1024*1024];
    private byte[] data2 = new byte[1024*1024];
    private byte[] data3 = new byte[1024*1024];
    private byte[] data4 = new byte[1024*1024];
    //饿汉式：类一启动就加载了对象
    private final static Hungry HUNGRY = new Hungry();
    private Hungry(){

    }

    public static Hungry getInstance(){
        return HUNGRY;
    }
}
```

为了克服饿汉式单例的内存空间浪费问题，于是有了懒汉式单例

但线程下，下面的代码是OK的

```java
package com.msj.lock.single;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private static LazySingle lazySingle;

    private LazySingle(){}

    public static LazySingle getInstance(){
        if(lazySingle==null){
            lazySingle = new LazySingle();
        }
        return lazySingle;
    }
}
```

多线程时

```java
package com.msj.lock.single;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private static LazySingle lazySingle;

    private LazySingle(){
        System.out.println(Thread.currentThread().getName() + " 创建了单例模式");
    }

    public static LazySingle getInstance(){
        if(lazySingle==null){
            lazySingle = new LazySingle();
        }
        return lazySingle;
    }

    //多线程并发
    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread(()->{
                LazySingle.getInstance();
            }).start();
        }
    }
}
```

会创建多个线程

![image-20210412101126770](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412101126770.png)

进阶：加锁（双重检测锁  DCL懒汉式）

```java
package com.msj.lock.single;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private static LazySingle lazySingle;

    private LazySingle(){
        System.out.println(Thread.currentThread().getName() + " 创建了单例模式");
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //多线程并发
    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread(()->{
                LazySingle.getInstance();
            }).start();
        }
    }
}
```

大部分情况下，都是一个对象

![image-20210412101522214](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412101522214.png)

极限情况下

```java
public static LazySingle getInstance(){
    //双重检测锁模式的懒汉式单例模式，又叫DCL模式
    if(lazySingle==null){
        //给LazySingle这个类上锁
        synchronized (LazySingle.class){
            if(lazySingle==null){
                lazySingle = new LazySingle();
                /*
                lazySingle = new LazySingle();不是原子性操作，需要经过下面的四个步骤：
                1、分配内存空间
                2、执行构造方法（初始化对象）
                3、把这个对象指向这个空间
                这时会发生指令重排现象，比如我们希望的指令执行顺序时123，有可能由于指令重排，执行顺序为132
                A线程 132 没有问题
                B线程 进来发现对象还没有指向空间，即为null，所以又会创建一个对象。
                */
            }
        }
    }
    return lazySingle;
}
```

为了防止指令重排，需加上volatile关键字(双重检测所+禁止指令重排)

```java
package com.msj.lock.single;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;

    private LazySingle(){
        System.out.println(Thread.currentThread().getName() + " 创建了单例模式");
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //多线程并发
    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread(()->{
                LazySingle.getInstance();
            }).start();
        }
    }
}
```



炫技时刻

方式三：静态内部类实现

```java
package com.msj.lock.single;

//静态内部类
public class Holder {
    private Holder(){}
    //静态内部类
    public static class InnerClass{
        private static final Holder HOLDER = new Holder();
    }

    //使用静态内部类创建对象
    public static Holder getInstance(){
        return InnerClass.HOLDER;
    }
}
```

这也是不安全的，因为Java中由反射的存在。

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;

    private LazySingle(){
        System.out.println(Thread.currentThread().getName() + " 创建了单例模式");
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        LazySingle lazySingle = LazySingle.getInstance();
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle);
        System.out.println(lazySingle1);
        System.out.println(lazySingle==lazySingle1);

    }
}
```

可以看到，两个对象已经不是一个了

![image-20210412103233584](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412103233584.png)

解决办法：解决反射破环单例

反射是通过无参构造创建的对象，我们在无参构造函数中也加入一把锁

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;

    private LazySingle(){
        synchronized (LazySingle.class){
            //如果对象已经存在就抛出一个异常
            if(lazySingle!=null){
                throw new RuntimeException("不要试图使用反射破环单例");
            }
        }
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        LazySingle lazySingle = LazySingle.getInstance();
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle);
        System.out.println(lazySingle1);
        System.out.println(lazySingle==lazySingle1);

    }
}
```

抛出异常

![image-20210412103631580](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412103631580.png)



还是存在问题，如果我两个对象都是用反射创建，单例仍然会被破环

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;

    private LazySingle(){
        synchronized (LazySingle.class){
            //如果对象已经存在就抛出一个异常
            if(lazySingle!=null){
                throw new RuntimeException("不要试图使用反射破环单例");
            }
        }
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //LazySingle lazySingle = LazySingle.getInstance();
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        LazySingle lazySingle2 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle1);
        System.out.println(lazySingle2);
        System.out.println(lazySingle1==lazySingle2);

    }
}
```

![image-20210412103847813](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412103847813.png)



这种情况下使用信号灯法解决:信号值是自己设置的，如果不通过反编译，一般是找不到这个变量的（如果在做一些加密，则会更安全）

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;
    //使用一个标志位
    private static boolean msj = false;

    private LazySingle(){
        synchronized (LazySingle.class){
            if(msj==false){
                msj = true;
            }else{
                //如果对象已经存在就抛出一个异常(如果对象存在，信号值msj会变为true，则执行下面的代码
                throw new RuntimeException("不要试图使用反射破环单例");
            }
        }
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //LazySingle lazySingle = LazySingle.getInstance();
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        LazySingle lazySingle2 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle1);
        System.out.println(lazySingle2);
        System.out.println(lazySingle1==lazySingle2);

    }
}
```

![image-20210412104345867](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412104345867.png)



假设msj这个变量被获取到了，仍然可以破环其单列

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;
    //使用一个标志位
    private static boolean msj = false;

    private LazySingle(){
        synchronized (LazySingle.class){
            if(msj==false){
                msj = true;
            }else{
                //如果对象已经存在就抛出一个异常(如果对象存在，信号值msj会变为true，则执行下面的代码
                throw new RuntimeException("不要试图使用反射破环单例");
            }
        }
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //LazySingle lazySingle = LazySingle.getInstance();
        //获得字段
        Field msj = LazySingle.class.getDeclaredField("msj");
        //破环msj的私有权限
        msj.setAccessible(true);
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        //把msj的值改为false
        msj.set(lazySingle1,false);
        LazySingle lazySingle2 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle1);
        System.out.println(lazySingle2);
        System.out.println(lazySingle1==lazySingle2);

    }
}
```

单例又被破环了

![image-20210412104822688](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412104822688.png)



怎么解决：通过分析源码，知枚举类型不能通过反射破环

![image-20210412104911736](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412104911736.png)

枚举

```java
package com.msj.lock.single;

//enum 枚举：本身也是一个Class类
public enum EnumSingle {
    INSTANCE;
    public EnumSingle getInstance(){
        return INSTANCE;
    }
}

class Test{
    public static void main(String[] args) {
        EnumSingle instance1 = EnumSingle.INSTANCE;
        EnumSingle instance2 = EnumSingle.INSTANCE;

        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

使用反射破环枚举

枚举类中也是由构造方法的，可以通过编译后的class文件查看

![image-20210412105352006](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412105352006.png)

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//enum 枚举：本身也是一个Class类
public enum EnumSingle {
    INSTANCE;
    public EnumSingle getInstance(){
        return INSTANCE;
    }
}

class Test{
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        EnumSingle instance1 = EnumSingle.INSTANCE;

        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);
        EnumSingle instance2 = declaredConstructor.newInstance();


        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

报错：没有空参的构造方法：java.lang.NoSuchMethodException

![image-20210412105700949](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412105700949.png)

idea骗了我们？

使用jdk反编译：也有空参构造，说明jdk也骗了我们（因为代码真正执行起来的时候是不会骗我们的）

![image-20210412105845908](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412105845908.png)



使用jad.exe反编译工具编译class文件，把源码生成Java文件

![image-20210412160517297](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412160517297.png)

编译后是有关构造器

![image-20210412160614528](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412160614528.png)

一个是String类型，一个是int类型

```java
package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//enum 枚举：本身也是一个Class类
public enum EnumSingle {
    INSTANCE;
    public EnumSingle getInstance(){
        return INSTANCE;
    }
}

class Test{
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        EnumSingle instance1 = EnumSingle.INSTANCE;

        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(String.class,int.class);
        declaredConstructor.setAccessible(true);
        EnumSingle instance2 = declaredConstructor.newInstance();


        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

直到反射不可以破坏枚举的单例：到此才直到，反射是不可以破环枚举的单例的。

![image-20210412160847118](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412160847118.png)



# 11、深入理解CAS

什么是CAS？

大厂必须深入研究底层，有所突破！

unsafe类

![image-20210412162342302](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412162342302.png)



![image-20210412162613022](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412162613022.png)

自旋锁

![image-20210412162705526](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412162705526.png)

CAS：ABA问题（狸猫换太子）

CAS：比较当前工作内存中的值和主存中的值，如果这个值是期望值，那么则执行操作，如果不是就一直循环

CAS的三个参数：期望的值 比较的值 更新的值

缺点：1、底层是自旋锁，循环耗时

2、一次性只能保证一个共享变量的原子性

3、ABA问题

```java
package com.msj.lock.cas;

import java.util.concurrent.atomic.AtomicInteger;

//
public class CASDemo {
    //CAS comparAndSet 比较并交换！
    public static void main(String[] args) {
        //原子类的Integer
        AtomicInteger atomicInteger = new AtomicInteger(2020);

        //public final boolean compareAndSet(int expect, int update)  参数：期望 更新
        //如果我期望的值达到了，就更新（更新为2021），否则不能修改成功,修改成功返回true，否则返回false
        //CAS，是CPU的并发原语
        System.out.println(atomicInteger.compareAndSet(2020, 2021));
        System.out.println(atomicInteger.get());

        //现在atomicInteger是2021，所以下面的代码不能执行成功
        atomicInteger.compareAndSet(2020,2022);
        System.out.println(atomicInteger.get());
    }
}
```



# 12、ABA问题

![image-20210412163651800](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412163651800.png)



```java
package com.msj.lock.cas;

import java.util.concurrent.atomic.AtomicInteger;

//
public class CASDemo {
    //CAS comparAndSet 比较并交换！
    public static void main(String[] args) {
        //原子类的Integer
        AtomicInteger atomicInteger = new AtomicInteger(2020);

        //public final boolean compareAndSet(int expect, int update)  参数：期望 更新
        //如果我期望的值达到了，就更新（更新为2021），否则不能修改成功,修改成功返回true，否则返回false
        //CAS，是CPU的并发原语
        //捣乱的线程 A
        System.out.println(atomicInteger.compareAndSet(2020, 2021));
        System.out.println(atomicInteger.get());

        //捣乱的线程 A
        System.out.println(atomicInteger.compareAndSet(2021, 2020));
        System.out.println(atomicInteger.get());

        //现在atomicInteger是2021，所以下面的代码不能执行成功
        //期望的线程 B
        atomicInteger.compareAndSet(2020,6666);
        System.out.println(atomicInteger.get());
    }
}
```

从上面的代码可知，线程将期望的值修改了两次（先改为2021后来又改为2020），但线程B并不知情。

![image-20210412164029613](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412164029613.png)

前面的操作像乐观锁。



解决办法：原子引用

带版本号的原子操作

![image-20210412164203969](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412164203969.png)

atomicReference:跟乐观锁一样

![image-20210412164248809](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412164248809.png)

```java
package com.msj.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

//
public class CASDemo {
    //CAS comparAndSet 比较并交换！
    public static void main(String[] args) {
        //原子类的Integer
        //AtomicInteger atomicInteger = new AtomicInteger(2020);

        //使用原子引用
        //new AtomicReference<>(),基本类，还有一个带时间戳的类，如下:参数一：期望值，参数二：被修改过就+1，相当于版本号
        AtomicStampedReference<Integer> integerAtomicStampedReference = new AtomicStampedReference<>(2020,1);

        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();//获得版本号
            System.out.println("a1-->" + stamp);
            //为了保证两个线程不同时执行，该线程延迟一会儿执行
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
        * public boolean compareAndSet(V   expectedReference,
                                 V   newReference,
                                 int expectedStamp,
                                 int newStamp)
         四个参数：参数三：期望的时间戳  参数四：新的（更新的值）时间戳
        * */
            integerAtomicStampedReference.compareAndSet(2020,2022,integerAtomicStampedReference.getStamp(),integerAtomicStampedReference.getStamp()+1);
            System.out.println("a2-->" + integerAtomicStampedReference.getStamp());

            integerAtomicStampedReference.compareAndSet(2020,2022,integerAtomicStampedReference.getStamp(),integerAtomicStampedReference.getStamp()+1);
            System.out.println("a3--> " + integerAtomicStampedReference.getStamp());
        },"a").start();

        //希望上面的线程修改过，下面的线程是知道的
        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();
            System.out.println("a2--> " + stamp);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            integerAtomicStampedReference.compareAndSet(2020,6666,stamp,stamp+1);
            System.out.println("b1-->" + integerAtomicStampedReference.getStamp());
        },"b").start();
    }
}
```

发现值并未被修改

![image-20210412170304459](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412170304459.png)

原因：

带版本号的原子操作，Integer使用了对象缓存机制，默认范围是-128-127，推荐使用静态工厂方法valueof获取对象实列，而不是new,因为valueOfhi用缓存，而new一定会创建新的对象分配新的内存空间。

![image-20210412170610522](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412170610522.png)

```java
package com.msj.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

//
public class CASDemo {
    //CAS comparAndSet 比较并交换！
    public static void main(String[] args) {
        //原子类的Integer
        //AtomicInteger atomicInteger = new AtomicInteger(2020);

        //使用原子引用
        //new AtomicReference<>(),基本类，还有一个带时间戳的类，如下:参数一：期望值，参数二：被修改过就+1，相当于版本号
        AtomicStampedReference<Integer> integerAtomicStampedReference = new AtomicStampedReference<>(1,1);

        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();//获得版本号
            System.out.println("a1-->" + stamp);
            //为了保证两个线程不同时执行，该线程延迟一会儿执行
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
        * public boolean compareAndSet(V   expectedReference,
                                 V   newReference,
                                 int expectedStamp,
                                 int newStamp)
         四个参数：参数三：期望的时间戳  参数四：新的（更新的值）时间戳
        * */
            System.out.println(integerAtomicStampedReference.compareAndSet(1, 2, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp() + 1));
            System.out.println("a2-->" + integerAtomicStampedReference.getStamp());

            System.out.println(integerAtomicStampedReference.compareAndSet(2, 1, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp() + 1));
            System.out.println("a3--> " + integerAtomicStampedReference.getStamp());
        },"a").start();

        //希望上面的线程修改过，下面的线程是知道的
        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();
            System.out.println("a2--> " + stamp);
            try {
                //后面的线程的延时应该更高一点
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(integerAtomicStampedReference.compareAndSet(1, 6, stamp, stamp + 1));
            System.out.println("b1-->" + integerAtomicStampedReference.getStamp());
        },"b").start();
    }
}
```

注意：AtomicStampedReference，如果泛型十一个包装类，要注意对象的引用问题（值的范围不要太大）

与乐观锁的原理相同。

![image-20210412171212887](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412171212887.png)

正常在业务操作，很少使用Integer，一般比较的是一个对象（如User）。

从上面知，使用原子引用可以解决

ABA问题。



# 13、可重入锁

1、公平锁、非公平锁

公平锁：非常公平，不能插队，必须先来后到

非公平锁：非常不公平，可以插队（默认都是非公平锁）

```java
public ReetrantLock(){
    sync = new NonfairSync();
}

//有参构造：可以修改使用的锁为公平锁还是非公平锁
public ReetrantLock(){
    sync = fair?new FiarSync():new NonfairSync();
}
```





2、可重入锁

所有的锁都是可重入锁（递归锁），拿到了外面的锁之后也就获得了里面的锁



![image-20210412172313688](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412172313688.png)



拿到外面的锁，里面的锁就自动拿到(synchronized锁)

```java
package com.msj.lock.demo01;

public class Demo01 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(()->{
            phone.sms();
        },"A").start();

        new Thread(()->{
            phone.sms();
        },"B").start();
    }
}

class Phone{
    public synchronized void sms(){
        System.out.println(Thread.currentThread().getName() + " sms");
        //call()中也有一把锁
        call();
    }
    public synchronized void call(){
        System.out.println(Thread.currentThread().getName() + " call");
    }
}
```

![image-20210412172925090](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412172925090.png)

lock锁

```java
package com.msj.lock.demo01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo02 {
    public static void main(String[] args) {
        Phone2 phone = new Phone2();
        new Thread(()->{
            phone.sms();
        },"A").start();

        new Thread(()->{
            phone.sms();
        },"B").start();
    }
}

class Phone2{
    Lock lock = new ReentrantLock();
    public void sms(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " sms");
            //call()中也有一把锁
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void call(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " call");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
```

线程A与线程B拿的是两把锁，锁使用的时候必须配对（即有加锁就必须有解锁，否则会造成死锁），同样也可以成功。

![image-20210412173422233](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412173422233.png)



3、自旋锁：spinlock,会不停的去试，直到成功

![image-20210412192332682](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412192332682.png)

自己写一个自旋锁，核心是CAS

SpinLockDemo.java

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

public class TestSpinLock {
    public static void main(String[] args) throws InterruptedException {
        //底层使用的自旋锁
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(()->{
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLockDemo.myUnLock();
            }
        },"T1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLockDemo.myUnLock();
            }
        },"T2").start();
    }
}
```

TestSplinkLock.java

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

public class TestSpinLock {
    public static void main(String[] args) throws InterruptedException {
        //底层使用的自旋锁
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(()->{
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLockDemo.myUnLock();
            }
        },"T1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLockDemo.myUnLock();
            }
        },"T2").start();
    }
}
```

![image-20210412194533880](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412194533880.png)

线程T1拿到所之后就自旋，只有等线程T1解锁了，线程T2才能解锁。





4、死锁

![image-20210412194718057](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412194718057.png)

两个线程互相争抢资源

```java
package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new MyThread(lockA,lockB),"T1").start();

        new Thread(new MyThread(lockB,lockA),"T2").start();
    }
}


class MyThread implements Runnable{
    private String lockA;
    private String lockB;

    public MyThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    public MyThread() {
    }

    @Override
    public void run() {
        //lockA想拿lockB，lockB想拿lockA
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName() + "  lock " + lockA + " => " + lockB);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName() + " lock " + lockB + " =>get " +lockA);
            }
        }
    }
}
```

死锁，程序一直不停止

![image-20210412195819959](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412195819959.png)

解决问题：

1、使用jps定位进程号

jps -l 显示活着的进程（在idea的命令行终端输入）

![image-20210412200120766](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412200120766.png)

2、查看死锁的进程信息

jstack 进程号

![image-20210412200251758](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412200251758.png)



![image-20210412200436159](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412200436159.png)

发现一个死锁

![image-20210412200507126](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412200507126.png)





![image-20210412200536968](https://typora-01.oss-cn-chengdu.aliyuncs.com/img/image-20210412200536968.png)

面试或者工作中，排查问题：

1、日志

2、堆栈信息
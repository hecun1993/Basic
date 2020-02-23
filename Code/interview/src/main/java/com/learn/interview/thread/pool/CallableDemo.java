package com.learn.interview.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t 进入实现Callable的线程");
        TimeUnit.SECONDS.sleep(3);
        return 1024;
    }
}

public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());

        // 开启了一个新的线程
        new Thread(futureTask, "AA").start();

        int mainResult = 10;
        Integer callableResult = futureTask.get();
        System.out.println("result = " + mainResult + callableResult);
    }
}
